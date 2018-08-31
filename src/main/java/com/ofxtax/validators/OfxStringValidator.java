package com.ofxtax.validators;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class OfxStringValidator {

    private static final Logger LOGGER = Logger.getLogger( OfxStringValidator.class.getSimpleName( ) );

    public static final String OUTPUT_ENCODING = "UTF-8";

    public static final String OFX_SCHEMA_FILE_NAME = "/xsd/OFX2Tax_Protocol.xsd";

    private static final URL OFX_SCHEMA_LOCATION_AS_URL = getSchemaUrl( OFX_SCHEMA_FILE_NAME );

    public static URL getSchemaUrl( String path ){
          URL xsdPathUrl = OfxStringValidator.class.getResource( path );
          return xsdPathUrl;
    }            
    
    private List<SAXParseException> exceptions; 
    
    
    public OfxStringValidator( ) {

    }

    public boolean validateVerbose( String xmlString ) {
    
        boolean isValid = validate( xmlString );
        
        if ( ! isValid ) {
            System.out.println( exceptionsText( ) );
        }
        
        return isValid;

    }

    public boolean validate( String xmlString ) {

        boolean useNameSpace = true;
        
        // Add namespace needed by validating parser
        if ( useNameSpace ) {
            String replace = "<OFX>";
            String replaceWith = "<ofx:OFX xmlns:ofx=\"http://ofx.net/types/2003/04\">";
            xmlString = xmlString.replaceFirst( replace, replaceWith );
            String replace2 = "</OFX>";
            String replaceWith2 = "</ofx:OFX>";
            xmlString = xmlString.replaceFirst( replace2, replaceWith2 );
        }
        
        Reader reader = new StringReader( xmlString );

        return validateReader( reader );
        
    }
    
    public boolean validateReader( Reader reader ) {
        
        try {
            
            SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
            
            // LOGGER.info( OFX_SCHEMA_LOCATION_AS_URL.toString( ) );
            
            Schema schema = factory.newSchema( OFX_SCHEMA_LOCATION_AS_URL );
            
            Validator validator = schema.newValidator( );

            exceptions = new LinkedList<SAXParseException>( );

            validator.setErrorHandler( new ErrorHandler( ) {
                
                @Override
                public void warning(SAXParseException e) throws SAXException
                {
                    exceptions.add( e );
                }

                @Override
                public void fatalError(SAXParseException e ) throws SAXException
                {
                    exceptions.add( e );
                }

                @Override
                public void error(SAXParseException e) throws SAXException
                {
                    exceptions.add( e );
                }
              
              } );            

            Source xmlSource = new StreamSource( reader );

            validator.validate( xmlSource );
            
        } catch ( Exception e ) {
            LOGGER.severe( "Exception: " + e.getMessage( ) );
        }
        
        if ( exceptions.size( ) > 0 ) {
            return false;
        }
        
        return true;
        
    }
    
    public String exceptionsText( ) {
        
        StringWriter stringWriter = new StringWriter( );
        
        for ( SAXParseException e : exceptions ) {
            int line = e.getLineNumber( );
            int col  = e.getColumnNumber( );
            String message = String.format( "Invalid OFX at line %d column %d: %s", line, col, e.getMessage( ) );
            stringWriter.append( message ).append( "\n" );
        }
        
        return stringWriter.toString( );
        
    }

}
