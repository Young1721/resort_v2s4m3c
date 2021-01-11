package dev.mvc.member;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component("dev.mvc.member.MemberProc")
public class MemberProc implements MemberProcInter {
  @Autowired
  private MemberDAOInter memberDAOInter;
  
  public MemberProc(){
    System.out.println("--> MemberProc created.");
  }

  @Override
  public int checkID(String id) {
    int cnt = this.memberDAOInter.checkID(id);
    return cnt;
  }

  @Override
  public int create(MemberVO memberVO) {
    int cnt = 0;
    cnt = this.memberDAOInter.create(memberVO);
    return cnt;
  }
}