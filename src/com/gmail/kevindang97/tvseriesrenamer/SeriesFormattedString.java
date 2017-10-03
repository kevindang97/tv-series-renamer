package com.gmail.kevindang97.tvseriesrenamer;

/**
 * Creates formatted strings intended for use as the file names of shows in a series  
 */
public class SeriesFormattedString {
	
	private static final String DEFAULT_FORMAT = "%t - %s%e[ - %n]";
	private static final int DEFAULT_PADDING = 2;
	
	private String originalFormat;
	private String intermediateFormat;
	private String optionalEpisodeNameFormat;
	private String seriesTitle;
	private int seasonNumber;
	private int seasonNumberPadding;
	private int episodeNumberPadding;
	
	public SeriesFormattedString() {
		this(DEFAULT_FORMAT, DEFAULT_PADDING, DEFAULT_PADDING);
	}
	
	public SeriesFormattedString(String format) {
		this(format, DEFAULT_PADDING, DEFAULT_PADDING);
	}
	
	public SeriesFormattedString(String format, int seasonPadding, int episodePadding) {
		this.originalFormat = "";
		this.intermediateFormat = "";
		this.optionalEpisodeNameFormat = "";
		this.seriesTitle = "";
		this.seasonNumber = 0;
		this.seasonNumberPadding = seasonPadding;
		this.episodeNumberPadding = episodePadding;
		setFormat(format);
	}
	
	/**
	 * Assigns a new format to the Formatter, special characters:
	 * %t - series title
	 * %s - season number
	 * %e - episode number
	 * [] - optional section for episode name, as this is optional  
	 * %n - episode name (encased inside square brackets
	 * @param format
	 */
	public void setFormat(String format) {
		int startOptionalSectionIndex = format.indexOf('[');
		int endOptionalSectionIndex = format.indexOf(']');
		
		if (startOptionalSectionIndex != -1 && endOptionalSectionIndex != -1) {
			// extract optional section of the format
			optionalEpisodeNameFormat = format.substring(startOptionalSectionIndex + 1, endOptionalSectionIndex);
			originalFormat = format.substring(0, startOptionalSectionIndex + 1);
			
			// add any extra bits after the optional section to the originalFormat if they exist
			if (format.length() > endOptionalSectionIndex) {
				originalFormat += format.substring(endOptionalSectionIndex);
			}
		} else {
			optionalEpisodeNameFormat = "";
			originalFormat = format;
		}
		
		generateIntermediateFormat();
	}
	
	/**
	 * Generates and sets a new intermediateFormat, which is used to save processing steps
	 * 	because in normal operation a bulk number of getFormattedString() calls will be made 
	 */
	private void generateIntermediateFormat() {
		if (seriesTitle != null) {
			intermediateFormat = originalFormat.replace("%t", seriesTitle);
			if (seasonNumber != 0) {
				intermediateFormat = intermediateFormat.replace("%s",
						String.format("0" + seasonNumberPadding + "d", seasonNumber));
			}
		} else {
			intermediateFormat = "";
		}
	}

	/**
	 * Set series title for the formatter
	 * @param seriesTitle
	 */
	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
		generateIntermediateFormat();
	}
	
	/**
	 * Set season number for the formatter
	 * @param seasonNumber
	 */
	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
		generateIntermediateFormat();
	}
	
	/**
	 * Set length of the 0 padding for the season number
	 * @param seasonNumberPadding
	 */
	public void setSeasonPadding(int seasonNumberPadding) {
		this.seasonNumberPadding = seasonNumberPadding;
		generateIntermediateFormat();
	}
	
	/**
	 * Set length of the 0 padding for the episode number
	 * @param episodeNumberPadding
	 */
	public void setEpisodePadding(int episodeNumberPadding) {
		this.episodeNumberPadding = episodeNumberPadding;
	}
	
	/**
	 * Returns the original format set earlier using setFormat()
	 * @return
	 */
	public String getOriginalFormat() {
		return originalFormat.replace("[]", optionalEpisodeNameFormat);
	}
	
	/**
	 * Main function, returns a formatted string built using the format, series title,
	 * 	season number and given episode number
	 * @param episodeNumber
	 * @return
	 */
	public String getFormattedString(int episodeNumber) {
		return intermediateFormat.replace("%e",
				String.format("0" + episodeNumberPadding + "d", episodeNumber));
	}
	
	/**
	 * Main function, returns a formatted string built using the format, series title,
	 * 	season number, given episode number and given episode name
	 * @param episodeNumber
	 * @return
	 */
	public String getFormattedString(int episodeNumber, String episodeName) {
		return getFormattedString(episodeNumber).replace("[]",
				optionalEpisodeNameFormat.replace("%n", episodeName));
	}
}
