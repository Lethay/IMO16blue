package AgentGridMin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bravorr on 10/24/16.
 */
public class FileIO {
    public final String fileName;
    public final char ReadWriteAppend;
    public final boolean binary;
    BufferedReader Reader;
    BufferedWriter Writer;
    DataOutputStream WriterBin;
    DataInputStream ReaderBin;
    public FileIO(String fileName,char rwa,boolean binary) {
        this.fileName=fileName;
        this.ReadWriteAppend=rwa;
        this.binary=binary;
        boolean writeOut=false;
        if(ReadWriteAppend=='a'){
            writeOut=true;
        }

        try {
            if (ReadWriteAppend == 'r') {
                if (this.binary) {
                    ReaderBin = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
                } else {
                    Reader = new BufferedReader(new FileReader(fileName));
                }
            } else if (ReadWriteAppend == 'w' || ReadWriteAppend == 'a') {
                if (this.binary) {
                    this.WriterBin = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName, writeOut)));
                } else {
                    Writer = new BufferedWriter(new FileWriter(fileName, writeOut));
                }
            }
            else{
                throw new IllegalArgumentException("rwa character must be one of r(read) w(write) or a(append)");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //READ FUNCTIONS
    public String[] ReadLineDelimit(String delimiter){
        String[] read=null;
        try{
            String line=Reader.readLine();
            if(line!=null){
               read=line.split(delimiter);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return read;
    }
    public ArrayList<String[]> ReadDelimit(String delimiter){
        ArrayList<String[]>lines=new ArrayList<String[]>();
        String[] read=ReadLineDelimit(delimiter);
        while(read!=null){
            lines.add(read);
            read=ReadLineDelimit(delimiter);
        }
        return lines;
    }
    public String ReadLine(){
        String read="";
        try {
            read=Reader.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
        return read;
    }
    public ArrayList<String> Read() {
        ArrayList<String>ret=new ArrayList<String>();
        String line=ReadLine();
        while(line!=null){
            ret.add(line);
            line=ReadLine();
        }
        return ret;
    }
    public int[]ReadLineIntDelimit(String delimeter){
        String[] raw=ReadLineDelimit(delimeter);
        int[] ret=new int[raw.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=Integer.parseInt(raw[i]);
        }
        return ret;
    }
    public float[]ReadLineFloatDelimit(String delimeter){
        String[] raw=ReadLineDelimit(delimeter);
        float[] ret=new float[raw.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=Float.parseFloat(raw[i]);
        }
        return ret;
    }
    public double[]ReadLineDoubleDelimit(String delimeter){
        String[] raw=ReadLineDelimit(delimeter);
        double[] ret=new double[raw.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=Double.parseDouble(raw[i]);
        }
        return ret;
    }
    public long[]ReadLineLongDelimit(String delimeter){
        String[] raw=ReadLineDelimit(delimeter);
        long[] ret=new long[raw.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=Long.parseLong(raw[i]);
        }
        return ret;
    }

    //WRITE FUNCTIONS
    public void Write(String line) {
        try {
            this.Writer.write(line);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void WriteDelimit(List<Object> data, String delimeter){
        if(data.size()>0) {
            for (int i = 0; i < data.size() - 1; i++) {
                Write(data.get(i).toString() + delimeter);
            }
            Write(data.get(data.size() - 1).toString());
        }
    }
    public void WriteDelimit(Object[] data,String delimiter){
        if(data.length>0) {
            for (int i = 0; i < data.length - 1; i++) {
                Write(data[i].toString() + delimiter);
            }
            Write(data[data.length - 1].toString());
        }
    }
    public void WriteDelimit(int[] data,String delimeter){
        if(data.length>0) {
            for (int i = 0; i < data.length - 1; i++) {
                Write(String.valueOf(data[i]) + delimeter);
            }
            Write(String.valueOf(data[data.length - 1]));
        }
    }
    public void WriteDelimit(long[] data,String delimeter){
        if(data.length>0) {
            for (int i = 0; i < data.length - 1; i++) {
                Write(String.valueOf(data[i]) + delimeter);
            }
            Write(String.valueOf(data[data.length - 1]));
        }
    }
    public void WriteDelimit(float[] data,String delimeter){
        if(data.length>0) {
            for (int i = 0; i < data.length - 1; i++) {
                Write(String.valueOf(data[i]) + delimeter);
            }
            Write(String.valueOf(data[data.length - 1]));
        }
    }
    public void WriteDelimit(double[] data,String delimeter){
        if(data.length>0) {
            for (int i = 0; i < data.length - 1; i++) {
                Write(String.valueOf(data[i]) + delimeter);
            }
            Write(String.valueOf(data[data.length - 1]));
        }
    }

    //BIN WRITE FUNCTIONS
    public void WriteBinDouble(double writeMe) {
        try{
            WriterBin.writeDouble(writeMe);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void WriteBinFloat(float writeMe) {
        try{
        WriterBin.writeFloat(writeMe);
    }
        catch(IOException e){
        e.printStackTrace();
    }
    }
    public void WriteBinInt(int writeMe) {
        try{
        WriterBin.writeInt(writeMe);
    }
        catch(IOException e){
                e.printStackTrace();
                }
    }
    public void WriteBinLong(long writeMe) {
        try{
        WriterBin.writeLong(writeMe);
        }
        catch(IOException e){
        e.printStackTrace();
        }
    }
    public void WriteBinString(String writeMe){
        try{
        WriterBin.writeChars(writeMe);
        }
        catch(IOException e){
        e.printStackTrace();
        }
    }
    public void WriteBinBoolean(boolean writeMe){
        try{
        WriterBin.writeBoolean(writeMe);
        }
        catch(IOException e){
        e.printStackTrace();
        }
    }

    //BIN READ FUNCTIONS
    public double ReadBinDouble() {
        double ret=0;
        try{
        ret=ReaderBin.readDouble();
        }
        catch(IOException e){
        e.printStackTrace();
        }
        return ret;
    }
    public float ReadBinFloat() {
        float ret=0;
        try{
        ret=ReaderBin.readFloat();
        }
        catch(IOException e){
        e.printStackTrace();
        }
        return ret;
    }
    public int ReadBinInt() {
        int ret=0;
        try{
        ret=ReaderBin.readInt();
        }
        catch(IOException e){
        e.printStackTrace();
        }
        return ret;
    }
    public long ReadBinLong() {
        long ret=0;
        try{
        ret=ReaderBin.readLong();
        }
        catch(IOException e){
        e.printStackTrace();
        }
        return ret;
    }
    public String ReadBinString(){
        String ret="";
        try{
        ret=ReaderBin.readUTF();
        }
        catch(IOException e){
        e.printStackTrace();
        }
        return ret;
    }
    public boolean ReadBinBoolean(){
        boolean ret=false;
        try{
        ret=ReaderBin.readBoolean();
        }
        catch(IOException e){
        e.printStackTrace();
        }
        return ret;
    }

    //CLOSE
    public void Close() {
        try {
            if(ReadWriteAppend=='r') {
                if (binary) {
                    ReaderBin.close();
                    ReaderBin=null;
                } else {
                    Reader.close();
                    Reader=null;
                }
            }
            else if (ReadWriteAppend == 'w'||ReadWriteAppend=='a') {
                if (binary) {
                    WriterBin.close();
                    WriterBin=null;
                } else {
                    Writer.close();
                    Writer=null;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
