package com.kitware.authorization.dao;

import java.util.List;

import com.kitware.authorization.vo.DocDetailVO;
import com.kitware.authorization.vo.DocVO;

public interface DocDAO {
	public int selectCount() throws Exception;//totalcnt값 가져옴
	public DocVO selectAll(String doc_num) throws Exception; //Document 내용 뿌려줌
	public List<DocDetailVO> selectConf(String doc_num) throws Exception; //Document에 대한 conf_num(결재자) 가져오기
	public List<DocVO> selectGJWait(String emp_num, int page) throws Exception; //내가 당장 결재 해야하는  문서 리스트
	public List<DocVO> selectIng(String id) throws Exception; //진행문서 select (내가 올린거)
	public List<DocVO> selectIng(String id, int page) throws Exception; //내가 올린 기안 다 보기(page 매개변수 추가)
	public List<DocVO> selectOk(String id) throws Exception; //완료문서 select (내가 올린거)  
	List<DocVO> selectOk(String id, int page) throws Exception;//내가 올린 완료문서 다 보기(page 매개변수 추가)
	public List<DocVO> selectExpected(String conf_num, int page) throws Exception; //언젠가 결재예정 select 
	public List<DocVO> selectGJOk(String conf_num, int page) throws Exception; //내가 결재완료한거(결재완료 사이드메뉴 해당)
	
	
	//다 구현한거 아니고 추가해야됩니다
	
	
}