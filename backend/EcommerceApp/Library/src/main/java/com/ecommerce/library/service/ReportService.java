package com.ecommerce.library.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {

    void generateExcel(HttpServletResponse httpServletResponse);
    
}
