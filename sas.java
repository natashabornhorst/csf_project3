import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
public class sas {

    static HashMap<String, Integer> labels = new HashMap<String, Integer>();
    static HashMap<String, Integer> opcodes = new HashMap<String, Integer>();
    static int counter = 0;
    static int line_counter = 0;
    static ByteArrayOutputStream bout;
    public static void main(String[] args)throws IOException {
        BufferedReader br = null;
        BufferedReader br2 = null;
        bout = new ByteArrayOutputStream(16);
        PrintWriter writer = new PrintWriter("loop.scram", "UTF-8");
        try {
            br = new BufferedReader(new FileReader("testfile.txt"));
            br2 = new BufferedReader(new FileReader("testfile.txt"));
            String line;
            setOpCodes();

            //first pass:
            while ((line = br.readLine()) != null) {
                if (readLine(line) == true) {
                    counter++;
                    line_counter++;
                }
                if (counter >= 16) {
                    System.err.println("Error: You have too many lines. 16 max.");
                    return;
                }
            }
            //second pass:
            counter = 0;
            line_counter = 0;
            while ((line = br2.readLine()) != null) {
                readLine2(line);
                counter++;
                line_counter++;
            }
            byte b [] = bout.toByteArray();

            for (int x = 0; x < b.length; x++) {
                writer.print((char)b[x]);
            }
            writer.close(); 

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null && br2 != null) {
                    br.close();
                    br2.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static boolean readLine(String line) {
        if (line.trim().isEmpty()) {
            return false;
        }
        String[] words1 = line.split(" ");
        
        ArrayList<String> words = removeSpaces(words1, false);

        if (words.size() == 0) {
            return false;
        } else if (isLabel(words.get(0)) && labels.containsKey(words.get(0))) {
            System.err.println("Error: Label has already been declared: line " + line_counter);
            return false;
        } else if (isLabel(words.get(0))) {
            String label = words.get(0).substring(0, words.get(0).length()-1);
            labels.put(label, counter);
        }
        return true;

    }

    public static int readLine2(String line)throws IOException {
        String output;
        if (line.trim().isEmpty()) {
            return 0;
        }
        String[] words1 = line.split(" ");

        ArrayList<String> words = removeSpaces(words1, true);

        if (words.size() == 0) {
            return 0;
        }

        //OPCODE
        int opcode;
        if (opcodes.containsKey(words.get(0))) {
            opcode = opcodes.get(words.get(0));
            System.out.print(opcode);
        }
        else {
            System.err.println("Error: operation does not exist. Line: " + line_counter);
            return 0;
        }

        //ADDRESS
        int address;
        if (labels.containsKey(words.get(1))) {
            address = labels.get(words.get(1));
                      System.out.print(address); 
        } else {
            System.out.print(words.get(1)); 
            String sAddress = words.get(1);
            try {
                address = Integer.valueOf(sAddress);
        
            } catch(NumberFormatException ex) {

                System.err.println("Error: You used a label that hasn't been declared. Line: " + line_counter);
                return 0;

            }
   

        }
        //check number of bits        
        if (words.get(0).compareTo("DAT") == 0) {
            //largest is 8 bit
            if (address > 255) {
                System.err.println("Error: Largest number of bits for DAT is 8. Line: " + line_counter);
            }
   
        } else {
            //largest is 4 bit
            if (address > 15) {
                System.err.println("Error: Largest number of bits is 4. Line: " + line_counter);
            }

        }

        write(opcode, address);

        return 1;

    }

    public static void write (int opcode, int address)throws IOException {

        String bin1 = Integer.toBinaryString(opcode);
        String bin2 = Integer.toBinaryString(address);
        String hex1 = binaryToHex(bin1);
        String hex2 = binaryToHex(bin2);
        bout.write(hex1.getBytes());
        bout.write(hex2.getBytes());
    }

    public static String binaryToHex(String bin) {
        return String.format("%21X", Long.parseLong(bin,2)) ;
    }




    /** gets rid of spaces and comments. */
    public static ArrayList<String> removeSpaces(String array[], boolean lbls) {
        ArrayList<String> newArray = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
        //System.out.println("start" + array[i] + "end");
            if (array[i].compareTo("") == 0) {
                //do nothing
            } else if (array[i].compareTo("#") == 0) {
                break;
            }
            else if (isLabel(array[i]) && lbls) {
                //do nothing
            }
            else {
                newArray.add(array[i]);
            }
            

        }
        return newArray;
    }

    /** checks if the string is a label declaration. */
    public static boolean isLabel(String name) {
        String lastChar = name.substring(name.length() -1);
        if (lastChar.compareTo(":") == 0) {
            return true; 
        } else {
            return false;
        }

    }

    public static void setOpCodes() {
        opcodes.put("HLT", 0); 
        opcodes.put("LDA", 1); 
        opcodes.put("LDI", 2); 
        opcodes.put("STA", 3); 
        opcodes.put("STI", 4); 
        opcodes.put("ADD", 5); 
        opcodes.put("SUB", 6); 
        opcodes.put("JMP", 7); 
        opcodes.put("JMZ", 8); 
        opcodes.put("DAT", 0); 
    }




}

