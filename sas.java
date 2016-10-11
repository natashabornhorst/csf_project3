import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class sas {

    static HashMap<String, Integer> labels = new HashMap<String, Integer>();
    static HashMap<String, Integer> opcodes = new HashMap<String, Integer>();
    static int counter = 0;
    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedReader br2 = null;

        try {
            br = new BufferedReader(new FileReader("testfile.txt"));
            br2 = new BufferedReader(new FileReader("testfile.txt"));
            String line;
            setOpCodes();

            //first pass:
            while ((line = br.readLine()) != null) {
  //System.out.println("line: " + line);
                if (readLine(line) == true) {
                    counter++;
                }
            }
            //second pass:
            counter = 0;
            while ((line = br2.readLine()) != null) {
                readLine2(line);
                counter++;
            }

 

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
        }
        else if (isLabel(words.get(0))) {
            String label = words.get(0).substring(0, words.get(0).length()-1);
            labels.put(label, counter);
        }
        return true;

    }

    public static int readLine2(String line) {
        if (line.trim().isEmpty()) {
            return 0;
        }
        String[] words1 = line.split(" ");

        ArrayList<String> words = removeSpaces(words1, true);

        if (words.size() == 0) {
            return 0;
        }

        //OPCODE
        int opcode = opcodes.get(words.get(0));
        System.out.print(opcode);
        
        //ADDRESS
        if (labels.containsKey(words.get(1))) {
            int address = labels.get(words.get(1));
            System.out.print(address); 
        } else {
            System.out.print(words.get(1)); 
        }


        return 1;

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

