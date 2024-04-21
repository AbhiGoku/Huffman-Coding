import java.io.*;
import java.util.*;

class Node implements Comparable<Node> {
  char character;
  int frequency;
  Node left, right;

  public Node(char character, int frequency) {
    this.character = character;
    this.frequency = frequency;
    left = right = null;
  }

  @Override
  public int compareTo(Node other) {
    return Integer.compare(this.frequency, other.frequency);
  }
}

public class Compress {
  private static final String MAGIC_WORD = "HUFFCOMP";
  private Map<Character, String> huffmanCodes = new HashMap<>();

  public void compress(String inputFile, String outputFile) throws IOException {
    FileInputStream fis = new FileInputStream(inputFile);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

    // Read input text and calculate character frequencies
    StringBuilder text = new StringBuilder();
    Map<Character, Integer> frequencies = new HashMap<>();
    int ch;
    while ((ch = br.read()) != -1) {
      char c = (char) ch;
      text.append(c);
      frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
    }
    br.close();

    // Build the Huffman tree
    PriorityQueue<Node> pq = new PriorityQueue<>();
    for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
      pq.add(new Node(entry.getKey(), entry.getValue()));
    }

    while (pq.size() > 1) {
      Node left = pq.poll();
      Node right = pq.poll();
      Node parent = new Node('\0', left.frequency + right.frequency);
      parent.left = left;
      parent.right = right;
      pq.add(parent);
    }

    buildHuffmanCodes(pq.peek(), "");

    // Compress the input file and write the output file
    try (FileOutputStream fos = new FileOutputStream(outputFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {

      // Write the magic word to identify the file as Huffman compressed
      oos.writeObject(MAGIC_WORD);
      // Write the Huffman code table to the output file
      oos.writeObject(huffmanCodes);
      // Encode the input text using Huffman codes
      StringBuilder encodedText = new StringBuilder();
      for (char c : text.toString().toCharArray()) {
        encodedText.append(huffmanCodes.get(c));
      }
      // Write the encoded text as a binary string to the output file
      oos.writeObject(encodedText.toString());
      System.out.println("Compression successful.");
    }
  }

  private void buildHuffmanCodes(Node root, String code) {
    if (root == null)
      return;
    if (root.left == null && root.right == null) {
      huffmanCodes.put(root.character, code);
    }
    buildHuffmanCodes(root.left, code + "0");
    buildHuffmanCodes(root.right, code + "1");
  }

  public static void main(String[] args) {
    Compress compressor = new Compress();
    try {
      String inputFile = "test.txt";
      String outputFile = "output.hzip";
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-f")) {
          inputFile = args[i + 1];
        } else if (args[i].equals("-o")) {
          outputFile = args[i + 1];
        }
      }
      if (inputFile == null) {
        System.out.println("Usage: java Compress -f file.txt [-o output.hzip -s]");
        return;
      }
      compressor.compress(inputFile, outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
