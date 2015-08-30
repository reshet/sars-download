package com.mresearch.spsstools.cup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HttpRequestPoster
{
  private static Reader reader;
  private static Writer writer;
  private static String rq;
  private static String ans;

  public String postData(String data, URL endpoint)
      throws Exception
  {
    rq = data;
    HttpURLConnection urlc = null;
    try
    {
      urlc = (HttpURLConnection)endpoint.openConnection();
      try
      {
        urlc.setRequestMethod("POST");
      }
      catch (ProtocolException e)
      {
        throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
      }
      urlc.setDoOutput(true);
      urlc.setDoInput(true);
      urlc.setUseCaches(false);
      urlc.setAllowUserInteraction(false);
      urlc.setRequestProperty("Content-type", "text/xml; charset=UTF-8");

      OutputStream out = urlc.getOutputStream();
      try
      {
        writer = new OutputStreamWriter(out, "UTF-8");
        pipe((byte)1);
        writer.close();
      }
      catch (IOException e)
      {
        throw new Exception("IOException while posting data", e);
      }
      finally {}
      InputStream in = urlc.getInputStream();
      try
      {
        reader = new InputStreamReader(in, "windows-1251");
        pipe((byte)2);
        reader.close();
      }
      catch (IOException e)
      {
        throw new Exception("IOException while reading response", e);
      }
      finally {}
    }
    catch (IOException e)
    {
      throw new Exception("Connection error (is server running at " + endpoint + " ?): " + e);
    }
    finally
    {
      if (urlc != null) {
        urlc.disconnect();
      }
    }
    return ans;
  }

  private static void pipe(byte mode)
      throws IOException
  {
    char[] buf = new char['E'];
    int read = 0;
    if (mode == 1)
    {
      writer.write(rq);
      writer.flush();
    }
    else
    {
      StringBuilder sbuild = new StringBuilder();
      while ((read = reader.read(buf)) >= 0) {
        sbuild.append(buf, 0, read);
      }
      ans = sbuild.toString();
    }
  }
}
