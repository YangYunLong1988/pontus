package com.snowstore.pontus.service.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.output.StringBuilderWriter;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class PDFBuilder {
	private PDFBuilder() {
	}

	private static Configuration cfg;

	static {
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setTemplateLoader(new ClassTemplateLoader(PDFBuilder.class, "/templates"));
		cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
	}

	public static byte[] createPDF(String name, Object obj) throws IOException, TemplateException, DocumentException {
		String html = getHtmlTemplate(name + ".html", obj);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont("/fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		String baseUrl = PDFBuilder.class.getResource("/images/").toString();
		renderer.setDocumentFromString(html, baseUrl);
		renderer.layout();
		renderer.createPDF(data);
		data.close();
		return data.toByteArray();
	}

	public static String getHtmlTemplate(String name, Object obj) throws IOException, TemplateException {
		Template temp = cfg.getTemplate(name);
		StringBuilderWriter sbw = new StringBuilderWriter();
		temp.process(obj, sbw);
		sbw.close();
		return sbw.getBuilder().toString();
	}
}
