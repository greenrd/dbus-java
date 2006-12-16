package test;

import java.util.Vector;

public class MethodCall extends Message
{
   MethodCall() { }
   public MethodCall(String dest, String path, String iface, String member, String sig, Object... args) 
   {
      super(Message.Endian.BIG, Message.MessageType.METHOD_CALL, (byte) 0);

      headers.put(Message.HeaderField.PATH,path);
      headers.put(Message.HeaderField.DESTINATION,dest);
      headers.put(Message.HeaderField.MEMBER,member);

      Vector<Object> hargs = new Vector<Object>();

      hargs.add(new Object[] { Message.HeaderField.PATH, new Object[] { ArgumentType.OBJECT_PATH_STRING, path } });
      hargs.add(new Object[] { Message.HeaderField.DESTINATION, new Object[] { ArgumentType.STRING_STRING, dest } });
      
      if (null != iface) {
         hargs.add(new Object[] { Message.HeaderField.INTERFACE, new Object[] { ArgumentType.STRING_STRING, iface } });
         headers.put(Message.HeaderField.INTERFACE,iface);
      }
      
      hargs.add(new Object[] { Message.HeaderField.MEMBER, new Object[] { ArgumentType. STRING_STRING, member } });

      if (null != sig) {
         hargs.add(new Object[] { Message.HeaderField.SIGNATURE, new Object[] { ArgumentType.SIGNATURE_STRING, sig } });
         headers.put(Message.HeaderField.SIGNATURE,sig);
         this.args = args;
      }

      byte[] blen = new byte[4];
      appendBytes(blen);
      append("ua(yv)", serial, hargs.toArray());
      pad((byte)8);

      long c = bytecounter;
      if (null != sig) append(sig, args);
      marshallint(bytecounter-c, blen, 0, 4);
   }
}