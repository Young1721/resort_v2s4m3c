package dev.mvc.member;
 
public interface MemberDAOInter {
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