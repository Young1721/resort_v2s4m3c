package dev.mvc.member;
 
public interface MemberProcInter {
  /**
   * �ߺ� ���̵� �˻�
   * @param id
   * @return �ߺ� ���̵� ����
   */
  public int checkID(String id);
  
  /**
   * ȸ�� ����
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
}