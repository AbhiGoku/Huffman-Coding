import java.io.*;
import java.util.*;

public class Decompress {
  private static final String MAGIC_WORD = "HUFFCOMP";

  public void decompress(String inputFile, String outputFile) throws IOException, ClassNotFoundException {
    FileInputStream fis = new FileInputStream(inputFile);
    ObjectInputStream ois = new ObjectInputStream(fis);

    // Read the magic word and verify the file format
    String magicWord = (String) ois.readObject();
    if (!magicWord.equals(MAGIC_WORD)) {
      System.out.println("Invalid compressed file. Missing magic word.");
      return;
    }

    // Read the Huffman code table from the compressed file
    Map<Character, String> huffmanCodes = (Map<Character, String>) ois.readObject();
    // Read the encoded text as a binary string
    String encodedText = (String) ois.readObject();
    ois.close();

    // Decompress the encoded text using the Huffman codes
    Map<String, Character> reverseHuffmanCodes = new HashMap<>();
    for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
      reverseHuffmanCodes.put(entry.getValue(), entry.getKey());
    }

    StringBuilder decodedText = new StringBuilder();
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < encodedText.length(); i++) {
      code.append(encodedText.charAt(i));
      if (reverseHuffmanCodes.containsKey(code.toString())) {
        decodedText.append(reverseHuffmanCodes.get(code.toString()));
        code.setLength(0);
      }
    }

    // Write the decompressed output file
    FileWriter writer = new FileWriter(outputFile);
    writer.write(decodedText.toString());
    writer.close();
    System.out.println("Decompression successful.");
  }

  public static void main(String[] args) {
    Decompress decompressor = new Decompress();
    try {
      String inputFile = "output.hzip";
      String outputFile = "output.txt";
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-f")) {
          inputFile = args[i + 1];
        } else if (args[i].equals("-o")) {
          outputFile = args[i + 1];
        }
      }
      if (inputFile == null) {
        System.out.println("Usage: java Decompress -f myfile.hzip [-o myfile.txt -s]");
        return;
      }
      decompressor.decompress(inputFile, outputFile);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
