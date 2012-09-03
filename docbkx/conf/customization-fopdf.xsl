<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xslthl="http://xslthl.sf.net"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="xslthl" version="1.0">

	<xsl:import href="urn:docbkx:stylesheet" />
	<xsl:import href="urn:docbkx:stylesheet/highlight.xsl"/>

	<xsl:param name="draft.watermark.image">
		images/draft.png
	</xsl:param>

    <!--###################################################
                  Paper & Page Size
   ################################################### -->

    <!-- Paper type, no headers on blank pages, no double sided printing -->
    <xsl:param name="paper.type" select="'A4'"/>
    <xsl:param name="double.sided">0</xsl:param>
    <xsl:param name="headers.on.blank.pages">0</xsl:param>
    <xsl:param name="footers.on.blank.pages">0</xsl:param>

    <!-- Space between paper border and content (chaotic stuff, don't touch) -->
    <xsl:param name="page.margin.top">5mm</xsl:param>
    <xsl:param name="region.before.extent">10mm</xsl:param>
    <xsl:param name="body.margin.top">10mm</xsl:param>

    <xsl:param name="body.margin.bottom">15mm</xsl:param>
    <xsl:param name="region.after.extent">10mm</xsl:param>
    <xsl:param name="page.margin.bottom">0mm</xsl:param>

    <xsl:param name="page.margin.outer">18mm</xsl:param>
    <xsl:param name="page.margin.inner">18mm</xsl:param>

    <!-- No intendation of Titles -->
    <xsl:param name="title.margin.left">0pc</xsl:param>

    <!--###################################################
                  Fonts & Styles
   ################################################### -->

    <!-- Left aligned text and no hyphenation -->
    <xsl:param name="alignment">justify</xsl:param>
    <xsl:param name="hyphenate">false</xsl:param>

    <!-- Default Font size -->
    <xsl:param name="body.font.master">11</xsl:param>
    <xsl:param name="body.font.small">8</xsl:param>

    <!-- Line height in body text -->
    <xsl:param name="line-height">1.4</xsl:param>

    <!-- Monospaced fonts are smaller than regular text -->
    <xsl:attribute-set name="monospace.properties">
        <xsl:attribute name="font-family">
            <xsl:value-of select="$monospace.font.family"/>
        </xsl:attribute>
        <xsl:attribute name="font-size">0.8em</xsl:attribute>
    </xsl:attribute-set>


	<!-- add page break after abstract block -->
	<xsl:attribute-set name="abstract.properties">
		<xsl:attribute name="break-after">page</xsl:attribute>
	</xsl:attribute-set>

	<!-- add page break after toc -->
	<xsl:attribute-set name="toc.margin.properties">
		<xsl:attribute name="break-after">page</xsl:attribute>
	</xsl:attribute-set>

	<!-- add page break after first level sections -->
	<xsl:attribute-set name="section.level1.properties">
		<xsl:attribute name="break-after">page</xsl:attribute>
	</xsl:attribute-set>

    <!-- Show only Sections up to level 3 in the TOCs -->
    <xsl:param name="toc.section.depth">2</xsl:param>

    <!-- Dot and Whitespace as separator in TOC between Label and Title-->
    <xsl:param name="autotoc.label.separator" select="'.  '"/>

	<!-- program listings / examples formatting -->
	<xsl:attribute-set name="monospace.verbatim.properties">
		<xsl:attribute name="font-family">Courier</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="keep-together.within-column">always</xsl:attribute>
	</xsl:attribute-set>

	<xsl:param name="shade.verbatim" select="1" />

	<xsl:attribute-set name="shade.verbatim.style">
		<xsl:attribute name="background-color">#E8E8E8</xsl:attribute>
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
		<xsl:attribute name="border-color">#575757</xsl:attribute>
		<xsl:attribute name="padding">3pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- callouts customization -->
	<xsl:param name="callout.unicode" select="1" />
	<xsl:param name="callout.graphics" select="0" />
    <xsl:param name="callout.defaultcolumn">90</xsl:param>	


</xsl:stylesheet>