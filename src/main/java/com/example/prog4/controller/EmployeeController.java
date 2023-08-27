package com.example.prog4.controller;

import com.example.prog4.config.CompanyConf;
import com.example.prog4.controller.mapper.EmployeeMapper;
import com.example.prog4.controller.validator.EmployeeValidator;
import com.example.prog4.model.employee.Employee;
import com.example.prog4.model.employee.EmployeeFilter;
import com.example.prog4.service.CSVUtils;
import com.example.prog4.service.EmployeeService;
import com.example.prog4.service.GeneratePDF;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/server/employee")
public class EmployeeController {
    private EmployeeMapper employeeMapper;
    private EmployeeValidator employeeValidator;
    private EmployeeService employeeService;
    private CompanyConf companyConf;

    @GetMapping("/list/csv")
    public ResponseEntity<byte[]> getCsv(HttpSession session) {
        EmployeeFilter filters = (EmployeeFilter) session.getAttribute("employeeFiltersSession");
        List<Employee> data = employeeService.getAll(filters).stream().map(employeeMapper::toView).toList();

        String csv = CSVUtils.convertToCSV(data);
        byte[] bytes = csv.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "employees.csv");
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("/list/filters/clear")
    public String clearFilters(HttpSession session) {
        session.removeAttribute("employeeFilters");
        return "redirect:/employee/list";
    }

    @PostMapping("/createOrUpdate")
    public String saveOne(@ModelAttribute Employee employee) {
        employeeValidator.validate(employee);
        com.example.prog4.repository.entity.employee.Employee domain = employeeMapper.toDomain(employee);
        employeeService.saveOne(domain);
        return "redirect:/employee/list";
    }

    @GetMapping("/generate/{id}")
    public String generatePdf(HttpServletResponse response, @PathVariable String id)
            throws IOException, DocumentException {

        Employee employee = employeeMapper.toView(employeeService.getOne(id));

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        int age = Period.between(employee.getBirthDate(), LocalDate.now()).getYears();

        Context context = new Context();
        context.setVariable("image", employee.getStringImage());
        context.setVariable("registrationNumber", employee.getRegistrationNumber());
        context.setVariable("name", employee.getLastName());
        context.setVariable("firstName", employee.getFirstName());
        context.setVariable("entranceDate", employee.getEntranceDate().toString());
        context.setVariable("departureDate", employee.getDepartureDate().toString());
        context.setVariable("cnaps", employee.getCnaps());
        context.setVariable("age", age);
        context.setVariable("companyLogo", companyConf.getLogo());
        context.setVariable("companyName", companyConf.getName());
        context.setVariable("companyNif", companyConf.getTaxIdentity().getNif());
        context.setVariable("companyStat", companyConf.getTaxIdentity().getStat());
        context.setVariable("companyAddress", companyConf.getAddress());
        context.setVariable("companyPhones", companyConf.getPhones());
        context.setVariable("companyEmail", companyConf.getEmail());
        context.setVariable("salary", employee.getSalary());

        String html = templateEngine.process("templates/invoices", context);

        String outputFolder = System.getProperty("user.home") + File.separator + "invoices.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();

        return "succes";
    }


}
