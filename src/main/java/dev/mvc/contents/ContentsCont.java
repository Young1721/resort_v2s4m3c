package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.cate.CateProcInter;
import dev.mvc.cate.CateVO;
import dev.mvc.categrp.CategrpProcInter;
import dev.mvc.categrp.CategrpVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
public class ContentsCont {
  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;

  @Autowired
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;

  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;

  public ContentsCont() {
    System.out.println("--> ContentsCont created.");
  }

  /**
   * ����� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/create.do", method = RequestMethod.GET)
  public ModelAndView create() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/create"); // /webapp/categrp/create.jsp

    return mav; // forward
  }

//http://localhost:9090/resort/contents/create.do
  /**
   * ��� ó��
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/contents/create.do", method = RequestMethod.POST)
  public ModelAndView create(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    contentsVO.setIp(request.getRemoteAddr()); // ������ IP
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    String file1 = ""; // main image
    String thumb1 = ""; // preview image

    String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
    // ���� ������ ����� fnamesMF ��ü�� ������.
    // <input type='file' class="form-control" name='file1MF' id='file1MF'
    // value='' placeholder="���� ����" multiple="multiple">
    MultipartFile mf = contentsVO.getFile1MF();
    long size1 = mf.getSize(); // ���� ũ��
    if (size1 > 0) { // ���� ũ�� üũ
      // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
      // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
      file1 = Upload.saveFileSpring(mf, upDir);

      if (Tool.isImage(file1)) { // �̹������� �˻�
        // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200, height: 150
        thumb1 = Tool.preview(upDir, file1, 200, 150);
      }

    }

    contentsVO.setFile1(file1);
    contentsVO.setThumb1(thumb1);
    contentsVO.setSize1(size1);
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------

    int cnt = this.contentsProc.create(contentsVO); // Call By Reference�� ����

    mav.addObject("cnt", cnt);
    mav.addObject("cateno", contentsVO.getCateno());
    mav.addObject("url", "create_msg"); // // webapp/contents/create_msg.jsp

    mav.setViewName("/contents/create_msg"); // /webapp/contents/create_msg.jsp

    return mav;
  }

  /**
   * ��� http://localhost:9090/resort/contents/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list_all.do", method = RequestMethod.GET)
  public ModelAndView list() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/list_all"); // /webapp/contents/list_all.jsp

    List<ContentsVO> list = this.contentsProc.list_all();
    mav.addObject("list", list);

    return mav; // forward
  }

  /**
   * ��� http://localhost:9090/resort/contents/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list.do", method = RequestMethod.GET)
  public ModelAndView list_by_cateno(int cateno) {
    ModelAndView mav = new ModelAndView();
    // ���̺� �̹��� ���, /webapp/contents/list_by_cateno.jsp
    mav.setViewName("/contents/list_by_cateno_table_img1");

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    List<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
    mav.addObject("list", list);

    return mav; // forward
  }
  
  /**
   * ��� http://localhost:9090/resort/contents/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list_by_cateno_grid1.do", method = RequestMethod.GET)
  public ModelAndView list_by_cateno_grid1(int cateno) {
    ModelAndView mav = new ModelAndView();
    
    // ���̺� �̹��� ���, /webapp/contents/list_by_cateno_grid1.jsp
    mav.setViewName("/contents/list_by_cateno_grid1");

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    List<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
    mav.addObject("list", list);

    return mav; // forward
  }

  // http://localhost:9090/resort/contents/read.do
  /**
   * ��ü ���
   * 
   * @return
   */
  @RequestMapping(value = "/contents/read.do", method = RequestMethod.GET)
  public ModelAndView read(int contentsno) {
    ModelAndView mav = new ModelAndView();

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

//    mav.setViewName("/contents/read"); // /webapp/contents/read.jsp
    mav.setViewName("/contents/read_img"); // /webapp/contents/read.jsp
    return mav;
  }

  // http://localhost:9090/resort/contents/update.do
  /**
   * ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/update.do", method = RequestMethod.GET)
  public ModelAndView update(int contentsno) {
    ModelAndView mav = new ModelAndView();

    ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // ������ �б�
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    mav.setViewName("/contents/update"); // webapp/contents/update.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/update.do
  /**
   * ���� ó��
   * 
   * @param contentsVO
   * @return
   */
  @RequestMapping(value = "/contents/update.do", method = RequestMethod.POST)
  public ModelAndView update(ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    // mav.addObject("cateVO", cateVO); // ���޾ȵ�.
    mav.addObject("cate_name", cateVO.getName());
    mav.addObject("cateno", cateVO.getCateno());

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    // mav.addObject("categrpVO", categrpVO); // ���޾ȵ�.
    mav.addObject("categrp_name", categrpVO.getName());

    mav.addObject("contentsno", contentsVO.getContentsno());

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����

    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����尡 ��ġ�� ��� �� ����
      cnt = this.contentsProc.update(contentsVO);
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����

    mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/delete.do
  /**
   * ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/delete.do", method = RequestMethod.GET)
  public ModelAndView delete(int contentsno) {
    ModelAndView mav = new ModelAndView();

    ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // ������ �б�
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    mav.setViewName("/contents/delete"); // webapp/contents/delete.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/delete.do
  /**
   * ���� ó�� + ���� ����
   * @param contentsVO
   * @return
   */
  @RequestMapping(value = "/contents/delete.do", method = RequestMethod.POST)
  public ModelAndView delete(HttpServletRequest request, 
                                        int cateno, 
                                        int contentsno, 
                                        String passwd) {
    ModelAndView mav = new ModelAndView();

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    String title = contentsVO.getTitle();
    mav.addObject("title", title);

    mav.addObject("contentsno", contentsno);

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����
    boolean sw= false;
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����尡 ��ġ�� ��� �� ����
      cnt = this.contentsProc.delete(contentsno);
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      sw = Tool.deleteFile(upDir, contentsVO.getFile1());  // Folder���� 1���� ���� ����
      sw = Tool.deleteFile(upDir, contentsVO.getThumb1());  // Folder���� 1���� ���� ����
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����

    mav.setViewName("/contents/delete_msg"); // webapp/contents/update_msg.jsp

    return mav;
  }

  /**
   * ����� http://localhost:9090/resort/contents/img_create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_create.do", method = RequestMethod.GET)
  public ModelAndView img_create(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/img_create"); // /webapp/contents/img_create.jsp

    ContentsVO contentsVO = this.contentsProc.read(contentsno); // ������ �б�
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    return mav; // forward
  }

  /**
   * ���� �̹��� ��� ó�� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_create.do", method = RequestMethod.POST)
  public ModelAndView img_create(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0;             // ������ ���ڵ� ���� 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // �н����尡 ��ġ�� ��� ���� ���ε�
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      String file1 = "";     // main image
      String thumb1 = ""; // preview image
          
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      // ���� ������ ����� fnamesMF ��ü�� ������.
      // <input type='file' class="form-control" name='file1MF' id='file1MF' 
      //           value='' placeholder="���� ����" multiple="multiple">
      MultipartFile mf = contentsVO.getFile1MF();
      long size1 = mf.getSize();  // ���� ũ��
      if (size1 > 0) { // ���� ũ�� üũ
        // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
        // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
        file1 = Upload.saveFileSpring(mf, upDir); 
        
        if (Tool.isImage(file1)) { // �̹������� �˻�
          // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1, 200, 150); 
        }
      }    
      
      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      
      mav.setViewName("redirect:/contents/read.do?contentsno=" + contentsVO.getContentsno());
      
      cnt = this.contentsProc.img_create(contentsVO);
      // mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����
            
    return mav;    
  }
  
  /**
   * ����� http://localhost:9090/resort/contents/img_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_update.do", method = RequestMethod.GET)
  public ModelAndView img_update(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/img_update"); // /webapp/contents/img_create.jsp

    ContentsVO contentsVO = this.contentsProc.read(contentsno); // ������ �б�
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    return mav; // forward
  }

  /**
   * ���� �̹��� ���� ó�� http://localhost:9090/resort/contents/img_delete.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_delete.do", method = RequestMethod.POST)
  public ModelAndView img_delete(HttpServletRequest request,
                                               int contentsno, int cateno, String passwd) {
    ModelAndView mav = new ModelAndView();
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0;             // ������ ���ڵ� ���� 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // �н����尡 ��ġ�� ��� ���� ���ε�
   // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      // ������ ���� ������ �о��.
      ContentsVO contentsVO = contentsProc.read(contentsno);
      // System.out.println("file1: " + contentsVO.getFile1());
      String file1 = contentsVO.getFile1().trim();
      String thumb1 = contentsVO.getThumb1().trim();
      long size1 = contentsVO.getSize1();
      boolean sw = false;
      
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      sw = Tool.deleteFile(upDir, contentsVO.getFile1());  // Folder���� 1���� ���� ����
      sw = Tool.deleteFile(upDir, contentsVO.getThumb1());  // Folder���� 1���� ���� ����
      // System.out.println("sw: " + sw);
      
      file1 = "";
      thumb1 = "";
      size1 = 0;
      
      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // ���� ���� ���� ����
      // -------------------------------------------------------------------
      
      mav.setViewName("redirect:/contents/read.do?contentsno=" + contentsVO.getContentsno());
      
      cnt = this.contentsProc.img_delete(contentsVO);
      // mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����
            
    return mav;    
  }
  
  /**
   * ���� �̹��� ���� ó�� http://localhost:9090/resort/contents/img_update.do
   * ���� �̹��� ������ ���ο� �̹��� ���(���� ó��)
   * @return
   */
  @RequestMapping(value = "/contents/img_update.do", method = RequestMethod.POST)
  public ModelAndView img_update(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0;             // ������ ���ڵ� ���� 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // �н����尡 ��ġ�� ��� ���� ���ε�
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      // ������ ���� ������ �о��.
      ContentsVO vo = contentsProc.read(contentsVO.getContentsno());
      // System.out.println("file1: " + contentsVO.getFile1());
      
      String file1 = vo.getFile1().trim();
      String thumb1 = vo.getThumb1().trim();
      long size1 = 0;
      boolean sw = false;
      
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      sw = Tool.deleteFile(upDir, contentsVO.getFile1());  // Folder���� 1���� ���� ����
      sw = Tool.deleteFile(upDir, contentsVO.getThumb1());  // Folder���� 1���� ���� ����
      // System.out.println("sw: " + sw);
      // -------------------------------------------------------------------
      // ���� ���� ���� ����
      // -------------------------------------------------------------------
      
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      // ���� ������ ����� fnamesMF ��ü�� ������.
      // <input type='file' class="form-control" name='file1MF' id='file1MF' 
      //           value='' placeholder="���� ����" multiple="multiple">
      MultipartFile mf = contentsVO.getFile1MF();
      size1 = mf.getSize();  // ���� ũ��
      if (size1 > 0) { // ���� ũ�� üũ
        // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
        // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
        file1 = Upload.saveFileSpring(mf, upDir); 
        
        if (Tool.isImage(file1)) { // �̹������� �˻�
          // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1, 200, 150); 
        }
      }    
      
      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------

      mav.setViewName("redirect:/contents/read.do?contentsno=" + contentsVO.getContentsno());
      
      cnt = this.contentsProc.img_create(contentsVO);
      // mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����
            
    return mav;    
  }

}
