package org.thunlp.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/*import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;*/
import org.thunlp.io.TextFileWriter;
import org.thunlp.misc.IntPair;
//import org.thunlp.tool.FolderReader;

public class Lexicon implements Serializable {
  private static final long serialVersionUID = 1L;

  protected static String COLON_REPLACER = "~CLN~";

  protected Hashtable<Integer, Word> idHash;
  protected Hashtable<String, Word> nameHash;
  protected boolean locked;
  protected long numDocs;
  
  public static String NUM_DOCS_STR = "";

  public Lexicon () {
    idHash = new Hashtable<Integer, Word>(50000);
    nameHash = new Hashtable<String, Word>(50000);
    locked = false;
    numDocs = 0;
  }
  
  public Lexicon(File f) {
    idHash = new Hashtable<Integer, Word>(50000);
    nameHash = new Hashtable<String, Word>(50000);
    locked = false;
    numDocs = 0;
    loadFromFile(f);
  }

  public void setLock( boolean locked ) {
    this.locked = locked;
  }

  public boolean getLock() {
    return this.locked;
  }

  public Word getWord( int id ) {
    return idHash.get ( id );
  }

  public Word getWord( String name ) {
    return nameHash.get ( name );
  }

  private Set<Integer> termSet = new HashSet<Integer>(256);
  public void addDocument ( String [] doc ) {
    termSet.clear();
    for ( String token : doc ) {
      Word t = nameHash.get(token);
      if ( t == null ) {
        if ( locked )
          continue;
        t = new Word();
        t.name = token;
        t.id = nameHash.size();
        t.tf = 0;
        t.df = 0;
        nameHash.put(t.name, t);
        idHash.put(t.id, t);
      }
      t.tf += 1;
      if ( ! termSet.contains(t.id) ) {
        termSet.add(t.id);
        t.df++;
      }
    }
    numDocs ++ ;
  }

  public Word [] convertDocument ( String [] doc ) {
    Word [] terms = new Word[doc.length];
    int n = 0;
    for ( int i = 0 ; i < doc.length ; i++ ) {
      String token = doc[i];
      Word t = nameHash.get( token );
      if ( t == null ) {
        if ( locked ) 
          continue;
        t = new Word ();
        t.name = token;
        t.tf = 1;
        t.df = 1;
        t.id = nameHash.size();
        nameHash.put(t.name, t);
        idHash.put(t.id, t);
      }
      terms[n++] = t;
    }
    if ( n < terms.length ) {
      Word [] finalterms = new Word[n];
      for ( int i = 0 ; i < n ; i++ ) {
        finalterms[i] = terms[i];
      }
      terms = finalterms;
    }
    return terms;
  }

  public int getSize() {
    return idHash.size();
  }

  public long getNumDocs() {
    return numDocs;
  }

  public boolean saveToFile( File f ) {
    try {
      FileOutputStream fos = new FileOutputStream( f );
      Enumeration<Word> e = nameHash.elements();
      String numDocsStr = numDocs + "\n";
      fos.write( numDocsStr.getBytes() );
      while ( e.hasMoreElements() ) {
        Word t = e.nextElement();
        String termString = t.toString() + "\n";
        fos.write( termString.getBytes("utf8") );
      }
      fos.close();
    } catch (FileNotFoundException e) {
      return false;
    } catch (UnsupportedEncodingException e) {
      return false;
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  public boolean loadFromInputStream(InputStream input) {
    nameHash.clear();
    idHash.clear();
    try {
      BufferedReader reader =
        new BufferedReader( new InputStreamReader( input, "UTF-8") );

      String termString;
      numDocs = Integer.parseInt(reader.readLine());
      while ( (termString = reader.readLine()) != null ) {
        Word t = buildWord( termString );
        if ( t != null ) {
          nameHash.put( t.name, t);
          idHash.put( t.id, t);
        }
      }
      reader.close();
    } catch (UnsupportedEncodingException e) {
      return false;
    } catch (IOException e) {
      return false;
    }
    return true;
  }
  
  public boolean loadFromFile( File f ) {
    FileInputStream fis;
    try {
      fis = new FileInputStream(f);
    } catch (FileNotFoundException e) {
      return false;
    }
    return loadFromInputStream(fis);
  }

  protected Word buildWord ( String termString ) {
    Word t = null;
    String [] parts = termString.split(":");
    if ( parts.length == 4 ) {
      t = new Word();
      t.id = Integer.parseInt(parts[0]);
      t.name = parts[1].replace(COLON_REPLACER, ":");
      t.tf = Integer.parseInt(parts[2]);
      t.df = Integer.parseInt(parts[3]);
    }
    return t;
  }

  public void mergeFrom(Lexicon another) {
    for (int i = 0; i < another.getSize(); i++) {
      Word other = another.getWord(i);
      Word local = this.getWord(other.name);
      if (local == null) {
        local = new Word();
        local.name = other.name;
        local.df = other.df;
        local.tf = other.tf;
        local.id = idHash.size();
        idHash.put(local.id, local);
        nameHash.put(local.name, local);
      } else {
        local.df += other.df;
        local.tf += other.tf;
      }
    }
    this.numDocs += another.numDocs;
  }
  
  /**
   * 紧缩词典，利用一个map把原来编号为key的word变为编号为value的word，去掉不在key
   * 中的word
   * @param translation 影射表
   */
  public Lexicon map( Map<Integer, Integer> translation ) {
    Lexicon newlex = new Lexicon();
    Hashtable<Integer, Word> newIdHash = new Hashtable<Integer, Word>();
    Hashtable<String, Word> newNameHash = new Hashtable<String, Word>();

    for ( Entry<Integer, Integer> e : translation.entrySet()){
      Word w = idHash.get(e.getKey());
      Word nw = (Word) w.clone();
      nw.id = e.getValue();
      newIdHash.put(nw.id, nw);
      newNameHash.put(nw.getName(), nw);
    }
    newlex.idHash = newIdHash;
    newlex.nameHash = newNameHash;
    newlex.numDocs = this.numDocs;
    return newlex;
  }

  /**
   * Remove words cover less than certain proportion of the whole corpus.
   * @param coverage A float number in [0, 1]
   * @return A new Lexicon object
   */
  public Lexicon removeLowCoverageWords(double coverage) {
    int minDf = (int) (numDocs * coverage);
    return removeLowDfWords(minDf);
  }
  
  /**
   * Remove words cover less than certain number of documents.
   * @param minDf minimal number of documents a word should occur in 
   * @return A new Lexicon object
   */
  public Lexicon removeLowDfWords(int minDf) {
    int id = 0;
    Hashtable<Integer, Integer> translation = new Hashtable<Integer, Integer>();
    for (Entry<Integer, Word> e : idHash.entrySet()) {
      Word w = e.getValue();
      if (w.df < minDf) {
        continue;
      }
      translation.put(w.id, id);
      ++id;
    }
    return map(translation);
  }
  
  /**
   * Remove words appear less than certain times.
   * @param minDf minimal number of documents a word should occur in 
   * @return A new Lexicon object
   */
  public Lexicon removeLowFreqWords(int minFreq) {
    int id = 0;
    Hashtable<Integer, Integer> translation = new Hashtable<Integer, Integer>();
    for (Entry<Integer, Word> e : idHash.entrySet()) {
      Word w = e.getValue();
      if (w.tf < minFreq) {
        continue;
      }
      translation.put(w.id, id);
      ++id;
    }
    return map(translation);
  }

  public Lexicon removeStopwords(Set<String> stopwords) {
    int id = 0;
    Hashtable<Integer, Integer> translation = new Hashtable<Integer, Integer>();
    for (Entry<Integer, Word> e : idHash.entrySet()) {
      Word w = e.getValue();
      if (!stopwords.contains(w.name)) {
        translation.put(w.id, id);
        ++id;
      }
    }
    return map(translation);
  }
  
  public Lexicon reorderWordsByFreq() {
    IntPair [] freq = new IntPair[idHash.size()];
    int n = 0;
    for (Entry<Integer, Word> e : idHash.entrySet()) {
      freq[n] = new IntPair(e.getKey(), e.getValue().tf);
      n++;
    }
    
    Arrays.sort(freq, new Comparator<IntPair>() {
      public int compare(IntPair o1, IntPair o2) {
        return o2.second - o1.second;
      }
    });
    
    Map<Integer, Integer> translation= new Hashtable<Integer, Integer>();
    for (int i = 0; i < freq.length; i++) {
      translation.put(freq[i].first, i);
    }
    return map(translation);
  }

  /**
   * 
   * @return
   */
  public String [] removeOov(String [] words) {
    List<String> output = new LinkedList<String>();
    for (String w : words) {
      if (this.getWord(w) != null) {
        output.add(w);
      }
    }
    return output.toArray(new String[output.size()]);
  }
  
 /* public static class LexiconReducer
  implements Reducer<Text, Text, Text, Text> {
    Text outvalue = new Text();
    
    public void reduce(Text key, Iterator<Text> values,
        OutputCollector<Text, Text> output, Reporter r) throws IOException {
      long sumTf = 0;
      long sumDf = 0;
      while (values.hasNext()) {
        String value = values.next().toString();
        int splitPoint = value.indexOf(' ');
        if (splitPoint > 0) {
          long tf = Long.parseLong(value.substring(0, splitPoint));
          long df = Long.parseLong(value.substring(splitPoint+1));
          sumTf += tf;
          sumDf += df;
        }
      }
      outvalue.set(sumTf + " " + sumDf);
      output.collect(key, outvalue);
    }

    public void configure(JobConf conf) {}

    public void close() throws IOException {}
    
  }
  
  public static void MakeLexiconFromSeqFile(Path seqFile, File lexicon)
  throws IOException {
    FolderReader reader = new FolderReader(seqFile);
    long numDocs = 0;
    ArrayList<String> wordInfos = new ArrayList<String>();
    Text key = new Text();
    Text value = new Text();
    long wordId = 0;
    while (reader.next(key, value)) {
      String valueStr = value.toString();
      int splitPoint = valueStr.indexOf(' ');
      if (splitPoint > 0) {
        long tf = Long.parseLong(valueStr.substring(0, splitPoint));
        long df = Long.parseLong(valueStr.substring(splitPoint+1));
        if (key.toString().equals(NUM_DOCS_STR)) {
          numDocs = df;
        } else {
          wordInfos.add(wordId + ":" + key.toString() + ":" + tf + ":" + df);
          wordId++;
        }
      }
    }
    reader.close();
    
    TextFileWriter writer =
      new TextFileWriter(lexicon.getAbsolutePath(), "UTF-8");
    writer.writeLine(Long.toString(numDocs));
    for (String wordInfo : wordInfos) {
      writer.writeLine(wordInfo);
    }
    writer.close();
  }*/
  
  public static class Word implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String name;
    protected int tf;  // global term frequency
    protected int df;  // global document frequency

    protected Word () {}

    protected Word ( int id, String name ) {
      this.id = id;
      this.name = name;
    }

    public String toString() {
      return id + ":" + name.replace(":", COLON_REPLACER) 
      + ":" + tf + ":" + df; 
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public int getFrequency() {
      return tf;
    }

    public int getDocumentFrequency() {
      return df;
    }


    public boolean equals( Object other ) {
      if ( ! (other instanceof Word) ) {
        return false;
      }
      Word ot = ( Word ) other;
      if ( ! ot.name.equals(name))
        return false;
      if ( ot.id != id )
        return false;
      return true;
    }

    protected Object clone() {
      Word t = new Word();
      t.name = name;
      t.id = id;
      t.tf = tf;
      t.df = df;
      return (Object) t;
    }

  }  
}
