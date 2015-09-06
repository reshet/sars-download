package com.mresearch.spsstools.web;

import com.mresearch.spsstools.beans.SpssLoadBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class SpssDownloadServlet extends HttpServlet {
  protected static final int DEFAULT_REQUEST_LIMIT_KB = 262144000;
  protected long maxSize = 262144000L;
  private SpssLoadBean loadBean = new SpssLoadBean();

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/octet-stream");

    String anketId = request.getParameter("anketId");

    String original_file_name = "spss_anket_" + anketId + "_ " + LocalDateTime.now() + ".sav";

    response.setHeader("Content-Disposition", "attachment; filename=\"" + original_file_name + "\"");

    byte[] arr = this.loadBean.loadFromMSS(anketId);
    response.getOutputStream().write(arr);
    response.getOutputStream().close();
  }
}
