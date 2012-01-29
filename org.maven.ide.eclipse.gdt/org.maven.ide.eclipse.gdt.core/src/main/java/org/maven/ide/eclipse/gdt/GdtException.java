package org.maven.ide.eclipse.gdt;

public class GdtException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public GdtException(String message) {
        super(message);
   }
    
    public GdtException(String message, Throwable throwable) {
        super(message, throwable);
   }

}
