package com.engagepoint.acceptancetest.base.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PDFWords {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PDFWords.class);

	private Map<Integer, String> pdfData;

	public void parsePdf(String filename) throws IOException {
		PdfReader reader = new PdfReader(filename);
		LOGGER.info("Reading file " + filename);
		pdfData = new HashMap<Integer, String>();
		int numberOfPages = reader.getNumberOfPages();
		for (int page = 1; page <= numberOfPages; page++) {
			LOGGER.info("Reading page " + page);
			String textFromPage = PdfTextExtractor.getTextFromPage(reader, page);
			pdfData.put(page, textFromPage);
		}
	}
	
	public void pdfShouldContain(String expectedValue) {
		Collection<String> values = pdfData.values();
		collectionShouldContain(expectedValue, values);
	}
	
	public void pdfShouldContainOccurrencesOfString(String expectedValue, int expectedCount) {
		Collection<String> values = pdfData.values();
		Assert.assertEquals(expectedCount, getCountMatchesInCollectiont(expectedValue, values));
	}

	public void pdfShouldContain(String expectedValue, String ignoreLinebreaks) {
		if (StringUtils.isEmpty(ignoreLinebreaks))
			pdfShouldContain(expectedValue);
		Collection<String> values = pdfData.values();
		values = removeLinebreaks(values);
		collectionShouldContain(expectedValue, values);
	}


	public void pdfShouldContain(String expectedValue, String ignoreLinebreaks,	String ignoreCase) {
		if (StringUtils.isEmpty(ignoreCase))
			pdfShouldContain(expectedValue, ignoreLinebreaks);
		Collection<String> values = pdfData.values();
		values = removeLinebreaks(values);
		collectionShouldContain(expectedValue, values, StringUtils.isNotEmpty(ignoreCase));
	}

	private void collectionShouldContain(String expectedValue, Collection<String> values) {
		collectionShouldContain(expectedValue, values, false);
	}

	private void collectionShouldContain(String expectedValue, Collection<String> values, boolean ignoreCase) {
		for (String content : values) {
			if (ignoreCase ? StringUtils.containsIgnoreCase(content, expectedValue) : StringUtils.contains(content,	expectedValue)) {
				return;
			}
		}
		throw new AssertionError("could not find " + expectedValue + " in " + pdfData);
	}
	
	private int getCountMatchesInCollectiont(String expectedValue, Collection<String> values) {
		int count = 0;
		for (String content : values) {
			count += StringUtils.countMatches(content, expectedValue);
		}		
		return count;
	}
	
	protected Collection<String> removeLinebreaks(Collection<String> values) {
		List<String> contentWithoutLinebreacks = new ArrayList<String>();
		for (String string : values) {
			String reformattedString = string.replaceAll("\\n", " ");
			while (reformattedString.contains("  ")) {
				reformattedString = reformattedString.replaceAll("  ", " ");
			}
			contentWithoutLinebreacks.add(reformattedString);
		}
		return contentWithoutLinebreacks;
	}

}
