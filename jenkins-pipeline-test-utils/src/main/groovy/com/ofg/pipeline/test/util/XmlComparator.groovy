package com.ofg.pipeline.test.util

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier
import org.custommonkey.xmlunit.XMLUnit
import org.junit.Before
import org.junit.ComparisonFailure

import java.nio.file.Paths

/**
 * @author Artur Gajowy
 * @author Marcin Grzejszczak
 */
//TODO package is as a separate test lib
//TODO rethink the way files are accessed in this class
trait XmlComparator {
    
    private XMLUnit xmlUnit
    
    @Before
    void init() {
        xmlUnit = new XMLUnit()
        xmlUnit.ignoreWhitespace = true
        xmlUnit.normalizeWhitespace = true
    }
    
    void compareXmls(File compared, Node nodeToCompare) {
        //default parameter initializers are not allowed in traits
        compareXmls(compared, nodeToCompare, false)
    }
    
    void compareXmls(File compared, Node nodeToCompare, boolean displayActualXmlInCaseOfError) {
        String nodeXml = XmlUtil.serialize(nodeToCompare).stripIndent().stripMargin()
        if (!compared.isFile()) {
            if (System.getProperty('outputMissingXml') == 'true') {
                File missingXml = compared
                missingXml.parentFile.mkdirs()
                missingXml.text = nodeXml
            }
            throw new RuntimeException("Reference xml file [${compared.path}] not found")
        }
        String referenceXml = XmlUtil.serialize(compared.text).stripIndent().stripMargin()
        compareXmls(compared, referenceXml, nodeXml, displayActualXmlInCaseOfError)
    }
    
    void compareXmls(File compared, String referenceXml, String nodeXml, boolean displayActualXmlInCaseOfError) {
        Diff diff = xmlUnit.compareXML(referenceXml, nodeXml)
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier())
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff)
            //TODO: How to get line from diff? Find by node in XML file?
            if (displayActualXmlInCaseOfError) {
                println("Actual XML:\n $nodeXml")
            }
            if (System.getProperty("outputActualXml") == 'true') {
                new File(compared.parentFile, createActualOutputFileName(compared)).text = nodeXml
            }
            throw new XmlsAreNotSimilar(compared.path, detailedDiff.allDifferences, referenceXml, nodeXml)
        }
    }
    
    private String createActualOutputFileName(File compared) {
        return compared.name.replace("xml", "ACTUAL.xml")
    }
    
    private File getFileOrNull(String path) {
        URI uri = getClass()?.getResource(path)?.toURI()
        uri ? new File(uri) : null
    }
    
    static class XmlsAreNotSimilar extends ComparisonFailure {
        XmlsAreNotSimilar(String packageFileName, List diffs, String expected, String actual) {
            super("For file ${formatPackageFileNameToHaveClickableLinkInIdea(packageFileName)} the following differences where found [$diffs].",
                  expected, actual)
        }
        
        private static String formatPackageFileNameToHaveClickableLinkInIdea(String packageFileName) {
            //.(foo.ext:1) is a regex recognizable by Idea
            //In addition as there usually is "at" word in the exception message later on it is required to add extra "at" before a file name
            return "at .(${Paths.get(packageFileName).fileName}:1) "
        }
    }
}
