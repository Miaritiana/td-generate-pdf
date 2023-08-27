package com.example.prog4.service;

import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

@Service
public class GeneratePDF {
    private String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("to", "Baeldung");

        return templateEngine.process("invoices", context);
    }

    public String generateThymeleafHtml() {
        // Code pour créer un modèle Thymeleaf et retourner le HTML généré
        // Utilisez la méthode parseThymeleafTemplate() ici
        // Par exemple :
        String thymeleafHtml = parseThymeleafTemplate();
        return thymeleafHtml;
    }
}
