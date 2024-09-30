package pe.edu.tecsup.gestordocumental.exception;

import java.io.Serial;

public class DocumentoNotFoundException extends  Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public DocumentoNotFoundException(String message)
    {
        super(message);
    }
}
