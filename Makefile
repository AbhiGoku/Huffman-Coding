# # Define variables
# JAVAC = javac
# JAVA = java
# SRC = huffman.java
# CLASS = huffman.class

# # Define targets
# all: compile

# compile:
# 	$(JAVAC) $(SRC)

# run:
# 	$(JAVA) huffman

# clean:
# 	rm -f $(CLASS)

all: compress decompress

compress:
	javac Compress.java
	java Compress

decompress:
	javac Decompress.java
	java Decompress

clean:
	rm *.class


