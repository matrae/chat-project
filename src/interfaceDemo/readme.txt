The purpose of this example is to demonstrate how to use interfaces
to allow "plug-and-play" replacement of components. In this case, we
have a simple program containing a checkbox. When the checkbox is
selected or deselected, the opponent is notified. This is meant to
represent a simple game, and these two functions (select, deselect)
are the two methods included in the interface.

The opponent can either be an AI or another program instance on
the network. Hence, the two components that can be initialized are
an AI module, or a network-communications module. Both components
support the same interface.

When the program is first started, the user is asked whether to
 (a) be a network server (initializes network component as server)
 (b) connect to a server (initializes network component as client)
 (c) play against the computer (initializes AI component)
 
 ARCHITECTURE
 ------------
 
 The interface is used in both directions:
 
 - The model calling the replaceable module declares the module by
   the interface: "private Comm opponent" Thus, it does not matter
   to the model which module is in use, since both support Comm
   
 - The module must be able to send the opponent's moves to the
   model. Hence, the model also supports the Comm interface,
   and is passed to the constructor of the opponent module. The
   opponent module only sees the Comm-defined methods, and uses
   these to communicate with the model.

 With the Java-9 module system, the module could be selected at
 run-time: This would be an implementation of the Strategy pattern.
 