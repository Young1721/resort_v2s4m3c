package dev.mvc.member;
 
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
public class MemberCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc = null;
  
  public MemberCont(){
    System.out.println("--> MemberCont created.");
  }
  
  // http://localhost:9090/resort/member/checkID.do?id=user1
   /**
  * ID 중복 체크, JSON 출력
  * @return
  */
  @ResponseBody
  @RequestMapping(value="/member/checkID.do", method=RequestMethod.GET ,
                             produces = "text/plain;charset=UTF-8" )
  public String checkID(String id) {
    int cnt = this.memberProc.checkID(id);
     
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
     
    return json.toString(); 
  }
  
  //http://localhost:9090/resort/member/create.do
  /**
  * 등록 폼
  * @return
  */
  @RequestMapping(value="/member/create.do", method=RequestMethod.GET )
  public ModelAndView create() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/member/create"); // webapp/member/create.jsp
   
    return mav; // forward
  }
  
  /**
   * 등록 처리
   * @param memberVO
   * @return
   */
  @RequestMapping(value="/member/create.do", method=RequestMethod.POST)
  public ModelAndView create(MemberVO memberVO){
    ModelAndView mav = new ModelAndView();
    
    // System.out.println("id: " + memberVO.getId());
    
    int cnt= memberProc.create(memberVO);
    mav.addObject("cnt", cnt); // redirect parameter 적용
    mav.addObject("url", "create_msg"); // create_msg.jsp, redirect parameter 적용
    
    mav.setViewName("redirect:/member/msg.do");
    
    return mav;
  }
  
  /**
   * 새로고침을 방지하는 메시지 출력
   * @param memberno
   * @return
   */
  @RequestMapping(value="/member/msg.do", method=RequestMethod.GET)
  public ModelAndView msg(String url){
    ModelAndView mav = new ModelAndView();
    
    // 등록 처리 메시지: create_msg --> /member/create_msg.jsp
    // 수정 처리 메시지: update_msg --> /member/update_msg.jsp
    // 삭제 처리 메시지: delete_msg --> /member/delete_msg.jsp
    mav.setViewName("/member/" + url); // forward
    
    return mav; // forward
  }
}