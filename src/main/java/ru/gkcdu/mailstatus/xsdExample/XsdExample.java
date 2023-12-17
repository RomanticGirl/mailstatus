package ru.gkcdu.mailstatus.xsdExample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import com.sun.codemodel.JCodeModel;

import org.xml.sax.InputSource;

public class XsdExample {

    public static void buildClasses(File xsdFile) throws IOException {
        SchemaCompiler sc = XJC.createSchemaCompiler();
        File schemaFile = new File(xsdFile.toURI());
        InputSource is = new InputSource(new FileInputStream(schemaFile));
        is.setSystemId(schemaFile.toURI().toString());
        sc.parseSchema(new InputSource(xsdFile.toURI().toString()));
        S2JJAXBModel model = sc.bind();

        JCodeModel cm = model.generateCode(null, null);
        cm.build(new File("."));
    }

    public static void main(String[] args) throws IOException {
        File xsdFile = new File("example.xsd");
        buildClasses(xsdFile);

    }
}