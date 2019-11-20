This package contains (or will hopefully contain) several programs demonstrating the "Jabberwocky" concept. If you are unfamiliar with the poem by that name, go read it and come back. The idea is to train a program on testual input data, and then ask the program to generate a text using similar statistical patterns.

The classic Jabberwocky example, which I have used for literally decades, works on a letter-by-letter basis: if "e" often follows "t" in the input text, then it will also do so in the output text. The text generation can be based on more than one character: what letter follows any sequence of 2, 3, or more letters? The beginning of the document must be a special start-tag, to initialize the generation process. Of course, the more letters used, the larger the training corpus must be, in order to create interesting texts.

This package also experiments with Jabberwocky on a word basis, and with bi-directional text generation. The ultimate program planned in the package is one designed to reply to spam mails. Given a spam mail telling you about the million-dollar inheritance you have from some Nigerian prince, it will generate random replies that could be sent back to the spammers, in hopes of wasting their time in an amusing fashion.

These examples use the Java Application Template, as discussed in class.

LICENSE: The license will not be specifically mentioned in the source files. However, you may consider all of these files to be licensed under the terms of the BSD 3-clause license (see the file license.txt).