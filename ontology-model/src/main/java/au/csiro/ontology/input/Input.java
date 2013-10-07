package au.csiro.ontology.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;


/**
 * Base class for inputs.
 *
 * @author Alejandro Metke
 */
public abstract class Input {

    /**
     * Indicates if the files should be loaded from an external file system or
     * the class path.
     *
     * @author Alejandro Metke
     *
     */
    public enum InputType {
        EXTERNAL, CLASSPATH
    };

    protected InputType inputType;
    protected String base;

    protected ZipFile zip = null;

    /**
     * @return the type
     */
    public InputType getInputType() {
        return inputType;
    }

    /**
     * @param type the type to set
     */
    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    /**
     * Optional base for resolving names against when {@code Input#getInputType()} is {@code InputType#EXTERNAL}.
     * <p>
     * If it resolves to a File, then it is treated as a ZIP file, otherwise it is treated as a directory.
     * @return
     */
    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public InputStream getInputStream(String name) throws IOException {
        final InputType type = getInputType();

        if (type.equals(InputType.EXTERNAL)) {
            File file;
            if (base != null) {
                File baseFile = new File(base);
                if (baseFile.isDirectory()) {
                    File child = new File(name);
                    if (child.isAbsolute()) {
                        file = child;                           // Absolute name
                    } else {
                        file = new File(baseFile, name);        // Relative name resolved against base
                    }
                } else {
                    zip = new ZipFile(base);                    // name resolved in Zip file
                    return zip.getInputStream(zip.getEntry(name));
                }
            } else {
                file = new File(name);                          // No base; default name resolution
            }

            return new FileInputStream(file);
        } else if (type.equals(InputType.CLASSPATH)) {
            final InputStream stream = this.getClass().getResourceAsStream(name);
            if (null == stream) {
                throw new FileNotFoundException(name + " not found in classpath");
            }
            return stream;
        } else {
            throw new RuntimeException("Unexpected input type " + type);
        }
    }

    /**
     * Release internal state.
     * <p>
     * Note that this will close any InputStreams linked to a base Zip file.
     */
    public void close() throws IOException {
        if (null != zip) {
            zip.close();
            zip = null;
        }
    }

}
