package com.gmail.kevindang97.tvseriesrenamer.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmail.kevindang97.tvseriesrenamer.SeriesFormatter;

public class SeriesFormattedStringTest {

	@Test
	public void getOriginalFormatTest() {
		SeriesFormatter sf = new SeriesFormatter();
		assertEquals("%t - s%se%e[ - %n]", sf.getOriginalFormat());
		
		sf = new SeriesFormatter("");
		assertEquals("", sf.getOriginalFormat());
		
		sf = new SeriesFormatter("%t - [%n - ] %s%e");
		assertEquals("%t - [%n - ] %s%e", sf.getOriginalFormat());
	}
	
	@Test
	public void getFormattedStringTest() {
		SeriesFormatter sf = new SeriesFormatter();
		assertEquals("", sf.getFormattedString(0));
		
		sf.setSeriesTitle("banana");
		assertEquals("banana - s%se04", sf.getFormattedString(4));
		
		sf.setSeasonNumber(3);
		assertEquals("banana - s03e05", sf.getFormattedString(5));
		assertEquals("banana - s03e42 - cheese", sf.getFormattedString(42, "cheese"));
		
		sf.setSeasonNumber(6);
		sf.setSeasonPadding(3);
		assertEquals("banana - s006e422 - danish", sf.getFormattedString(422, "danish"));
		
		sf.setFormat("%t - Season %s Episode %e");
		sf.setSeasonNumber(42);
		sf.setSeasonPadding(1);
		sf.setEpisodePadding(1);
		for (int i = 1; i < 27; i++) {
			assertEquals("banana - Season 42 Episode " + i, sf.getFormattedString(i, "esprit"));
		}
	}

}
