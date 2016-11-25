package com.ihordev.helloservice.dao.impl;

import java.io.IOException;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.ClassPathResource;

/**
 * This is a helper class for loading test data XML resources in database tests.
 */
final class FlatXmlDataSetHelper
{

    private FlatXmlDataSetHelper()
    {
        throw new AssertionError("FlatXmlDataSetHelper class can not be instantiated");
    }

    /**
     * Loads data set from XML resource with corresponding meta data from DTD.
     * 
     * @param   dataSetResourcePath     path to XML resource that contains test data.
     * @param   dataSetDTDResourcePath  path to DTD file that defines data structure.
     * 
     * @return                          dataset for {@link org.dbunit.operation.DatabaseOperation} 
     *                                  for testing.
     * 
     * @throws  DataSetException
     * @throws  IOException
     */
    public static IDataSet getDataSet(String dataSetResourcePath, String dataSetDTDResourcePath)
            throws DataSetException, IOException
    {
        FlatXmlDataSetBuilder FlatXmlBuilder = new FlatXmlDataSetBuilder();

        ClassPathResource dataSetDTDResource = new ClassPathResource(dataSetDTDResourcePath);
        FlatXmlBuilder.setMetaDataSetFromDtd(dataSetDTDResource.getInputStream());

        ClassPathResource dataSetResource = new ClassPathResource(dataSetResourcePath);
        FlatXmlDataSet dataSet = FlatXmlBuilder.build(dataSetResource.getInputStream());

        return dataSet;
    }

}
