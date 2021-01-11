package dev.mvc.cate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.categrp.CategrpProcInter;
import dev.mvc.categrp.CategrpVO;

@Controller
public class CateCont {
  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;
  
  @Autowired
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;

  public CateCont() {
    System.out.println("--> CateCont created.");
  }

  /**
   * ����� http://localhost:9090/resort/cate/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/create.do", method = RequestMethod.GET)
  public ModelAndView create() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/create"); // /cate/create.jsp

    return mav;
  }

  /**
   * ���ó��
   * http://localhost:9090/resort/cate/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/create.do", method = RequestMethod.POST)
  public ModelAndView create(CateVO cateVO) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/create_msg");

    int cnt = this.cateProc.create(cateVO);
    mav.addObject("cnt", cnt);

    return mav;
  }
  
  /**
   * ��� http://localhost:9090/resort/cate/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/list_all.do", method = RequestMethod.GET)
  public ModelAndView list_all() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/list_all"); // /webapp/cate/list.jsp

    // List<CateVO> list = this.cateProc.list_cateno_asc();
    List<CateVO> list = this.cateProc.list_seqno_asc();
    mav.addObject("list", list);

    return mav; // forward
  }
  
  /**
   * ��� http://localhost:9090/resort/cate/list.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/list.do", method = RequestMethod.GET)
  public ModelAndView list(int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/list"); // /webapp/cate/list.jsp
    
    // Categrp ����
    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);
    
    // List<CateVO> list = this.cateProc.list_cateno_asc();
    List<CateVO> list = this.cateProc.list_by_categrpno(categrpno);
    mav.addObject("list", list);

    return mav; // forward
  }

  /**
   * ��ȸ + ������ http://localhost:9090/resort/cate/read_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/read_update.do", method = RequestMethod.GET)
  public ModelAndView read_update(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/read_update"); // /webapp/cate/read_update.jsp

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    List<CateVO> list = this.cateProc.list_by_categrpno(categrpno);
    mav.addObject("list", list);

    return mav; // forward
  }
  
  /**
   * ���� ó��
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/cate/update.do", method = RequestMethod.POST)
  public ModelAndView update(CateVO cateVO) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update(cateVO);
    mav.addObject("cnt", cnt); // request�� ����

    mav.setViewName("/cate/update_msg"); // webapp/cate/update_msg.jsp

    return mav;
  }
  
  /**
   * ��ȸ + ������ http://localhost:9090/resort/cate/read_delete.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/read_delete.do", method = RequestMethod.GET)
  public ModelAndView read_delete(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/read_delete"); // /webapp/cate/read_delete.jsp

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    List<CateVO> list = this.cateProc.list_by_categrpno(categrpno);
    mav.addObject("list", list);

    return mav; // forward
  }
  
  /**
   * ���� ó��
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/cate/delete.do", method = RequestMethod.POST)
  public ModelAndView delete(int cateno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.delete(cateno);
    mav.addObject("cnt", cnt); // request�� ����

    mav.setViewName("/cate/delete_msg"); // webapp/cate/delete_msg.jsp

    return mav;
  }
  
  /**
   * �켱���� ���� up 10 �� 1
   * @param cateno ī�װ� ��ȣ
   * @return
   */
  @RequestMapping(value="/cate/update_seqno_up.do", 
                              method=RequestMethod.GET )
  public ModelAndView update_seqno_up(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update_seqno_up(cateno);
    // mav.addObject("cnt", cnt); // request�� ����
    
    mav.setViewName("redirect:/cate/list.do?categrpno="+categrpno); 

    return mav;
  }
  
  /**
   * �켱���� ���� up 1 �� 10
   * @param cateno ī�װ� ��ȣ
   * @return
   */
  @RequestMapping(value="/cate/update_seqno_down.do", 
                              method=RequestMethod.GET )
  public ModelAndView update_seqno_down(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update_seqno_down(cateno);
    // mav.addObject("cnt", cnt); // request�� ����
    
    mav.setViewName("redirect:/cate/list.do?categrpno="+categrpno);

    return mav;
  }
  
  /**
   * ��¸�� ����
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/cate/update_visible.do", method = RequestMethod.GET)
  public ModelAndView update_visible(CateVO cateVO) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update_visible(cateVO);
    mav.addObject("cnt", cnt); // request�� ����

    if (cnt == 1) { 
      mav.setViewName("redirect:/cate/list.do?categrpno="+cateVO.getCategrpno()); // request ��ü�� ������ �ȵ�. 
    } else {
      CateVO vo = this.cateProc.read(cateVO.getCateno());
      String name = vo.getName();
      mav.addObject("name", name);
      mav.setViewName("/cate/update_visible_msg"); // /cate/update_visible_msg.jsp
    }
    return mav;
  }
  
}




