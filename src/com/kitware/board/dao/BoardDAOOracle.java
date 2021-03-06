package com.kitware.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import com.kitware.board.vo.DepartBoard;
import com.kitware.board.vo.Comment;
import com.kitware.board.vo.NoticeBoard;
import com.kitware.board.vo.PhotoBoard;
import com.kitware.sql.MyConnection;
import com.kitware.member.vo.Members;



public class BoardDAOOracle implements BoardDAO {

	@Override
	public int selectCount() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectCountSQL = "SELECT COUNT(*) totalcnt FROM notice_board";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectCountSQL);
			rs = pstmt.executeQuery();
			rs.next();
			int totalCount = rs.getInt("totalcnt");
			return totalCount;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public List<NoticeBoard> selectNoticeBoard(int page) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectNBSQL = "select r,seq,emp_num,name,title,content,hit,to_char(log_time,'yyyy-mm-dd hh24:mi') log_time ,originfilename,savefilename , path "
				+ "from(select rownum r ,a.* " + "    from(select * " + "    from notice_board "
				+ "    order by seq desc)a)b " + "where r between ? and ? ";
		List<NoticeBoard> list = new ArrayList<NoticeBoard>();
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectNBSQL);
			int cntPerPage = 4;// 1페이지별 3건씩 보여준다
			int endRow = cntPerPage * page;
			int startRow = endRow - cntPerPage + 1;
			System.out.println("startRow:" + startRow);
			System.out.println("endRow:" + endRow);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new NoticeBoard(rs.getString("r"), rs.getString("seq"), rs.getString("emp_num"),
						rs.getString("name"), rs.getString("title"), rs.getString("content"), rs.getString("hit"),
						rs.getString("log_time"), rs.getString("originfilename"), rs.getString("savefilename"),
						rs.getString("path")));
			}
			return list;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public void insertNoticeBoard(NoticeBoard noticeBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String insertNBSQL = "INSERT INTO notice_board (seq , emp_num, name, title, content, log_time,originfilename,savefilename,path) "
				+ "VALUES (noticeboard_seq.nextval,?,?,?,?,sysdate,?,?,?)";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(insertNBSQL);
			pstmt.setString(1, noticeBoard.getEmp_num());
			pstmt.setString(2, noticeBoard.getName());
			pstmt.setString(3, noticeBoard.getTitle());
			pstmt.setString(4, noticeBoard.getContent());
			pstmt.setString(5, noticeBoard.getOriginFileName());
			pstmt.setString(6, noticeBoard.getSaveFileName());
			pstmt.setString(7, noticeBoard.getPath());
			pstmt.executeUpdate();

		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public void updateNoticeBoard(NoticeBoard noticeBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updateNBSQL = "update notice_board \r\n" + "set title= ? , content = ? , log_time = sysdate\r\n"
				+ "where seq = ?";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateNBSQL);
			pstmt.setString(1, noticeBoard.getTitle());
			pstmt.setString(2, noticeBoard.getContent());
			pstmt.setString(3, noticeBoard.getSeq());
			pstmt.executeUpdate();
			System.out.println("글 번호:" + noticeBoard.getSeq());
			System.out.println("바뀐 타이틀 : " + noticeBoard.getTitle());
			System.out.println("바뀐 내용:" + noticeBoard.getContent());
			System.out.println("성공했다면 update가 잘 되어야 하는데 왜그러니...?");
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public void deleteNoticeBoard(String seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String deleteNBSQL = "delete notice_board\r\n" + "where seq = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(deleteNBSQL);
			pstmt.setString(1, seq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	// 조회수를 1씩 증가한다.
	@Override
	public void updateHit(String hitseq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		String updateHitSQL = "update notice_board " + "set hit = hit + 1 " + "where seq = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateHitSQL);
			pstmt.setString(1, hitseq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}

	}

	// 글 수정 시 파일에 변화가 있을때 update
	@Override
	public void updateNoticeBoardFile(NoticeBoard noticeBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updateNBSQL = "update notice_board\r\n"
				+ "set title= ? , content =? , log_time = sysdate, originfilename = ? ,savefilename = ? , path = ?\r\n"
				+ "where seq = ?";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateNBSQL);
			pstmt.setString(1, noticeBoard.getTitle());
			pstmt.setString(2, noticeBoard.getContent());
			pstmt.setString(3, noticeBoard.getOriginFileName());
			pstmt.setString(4, noticeBoard.getSaveFileName());
			pstmt.setString(5, noticeBoard.getPath());
			pstmt.setString(6, noticeBoard.getSeq());
			pstmt.executeUpdate();

		} finally {
			MyConnection.close(pstmt, con);
		}

	}

	// 이전글 찾기
	@Override
	public NoticeBoard selectPrePost(String seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectPreSQL = "select seq,emp_num,name,title,content,hit,to_char(log_time,'yyyy-mm-dd hh24:mi') log_time ,originfilename,savefilename , path\r\n"
				+ "from notice_board\r\n" + "where seq = (select pre_seq from\r\n" + "            (\r\n"
				+ "            select seq,\r\n" + "            lag(seq,1,0) over (order by seq) pre_seq "
				+ "            from notice_board\r\n" + "            )\r\n" + "            where seq= ?)";
		NoticeBoard nb = new NoticeBoard();
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectPreSQL);
			pstmt.setString(1, seq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nb.setSeq(rs.getString("seq"));
				nb.setEmp_num(rs.getString("emp_num"));
				nb.setName(rs.getString("name"));
				nb.setTitle(rs.getString("title"));
				nb.setContent(rs.getString("content"));
				nb.setHit(rs.getString("hit"));
				nb.setLog_time(rs.getString("log_time"));
				nb.setOriginFileName(rs.getString("originfilename"));
				nb.setSaveFileName(rs.getString("savefilename"));
				nb.setPath(rs.getString("path"));
			}
			return nb;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	// 다음글 찾기
	@Override
	public NoticeBoard selectNextPost(String seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectNextSQL = "select seq,emp_num,name,title,content,hit,to_char(log_time,'yyyy-mm-dd hh24:mi') log_time ,originfilename,savefilename , path\r\n"
				+ "from notice_board\r\n" + "where seq = (select next_seq from\r\n" + "            (\r\n"
				+ "            select seq,\r\n" + "            lead(seq,1,0) over (order by seq) next_seq "
				+ "            from notice_board\r\n" + "            )\r\n" + "            where seq= ?)";
		NoticeBoard nb = new NoticeBoard();
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(selectNextSQL);
			pstmt.setString(1, seq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nb.setSeq(rs.getString("seq"));
				nb.setEmp_num(rs.getString("emp_num"));
				nb.setName(rs.getString("name"));
				nb.setTitle(rs.getString("title"));
				nb.setContent(rs.getString("content"));
				nb.setHit(rs.getString("hit"));
				nb.setLog_time(rs.getString("log_time"));
				nb.setOriginFileName(rs.getString("originfilename"));
				nb.setSaveFileName(rs.getString("savefilename"));
				nb.setPath(rs.getString("path"));
			}
			return nb;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	// 사진게시판 글 전부 찾아오기.
	@Override
	public List<PhotoBoard> selectPhotoBoard(int page) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectNBSQL = "select r,seq,emp_num,name,title,content,hit,to_char(log_time,'yyyy-mm-dd hh24:mi') log_time ,originfilename,savefilename , path "
				+ "from(select rownum r ,a.* " + "from(select * " + "from photo_board " + "order by seq desc)a)b "
				+ "where r between ? and ? ";
		List<PhotoBoard> list = new ArrayList<PhotoBoard>();
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectNBSQL);
			int cntPerPage = 4;// 1페이지별 3건씩 보여준다
			int endRow = cntPerPage * page;
			int startRow = endRow - cntPerPage + 1;
			System.out.println("startRow:" + startRow);
			System.out.println("endRow:" + endRow);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new PhotoBoard(rs.getString("r"), rs.getString("seq"), rs.getString("emp_num"),
						rs.getString("name"), rs.getString("title"), rs.getString("content"), rs.getString("hit"),
						rs.getString("log_time"), rs.getString("originfilename"), rs.getString("savefilename"),
						rs.getString("path")));
			}
			return list;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	// 사진게시판 글 추가
	@Override
	public void insertPhotoBoard(PhotoBoard photoBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String insertPBSQL = "INSERT INTO photo_board (seq , emp_num, name, title, content, log_time,originfilename,savefilename,path) "
				+ "VALUES (photoboard_seq.nextval,?,?,?,?,sysdate,?,?,?)";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(insertPBSQL);
			pstmt.setString(1, photoBoard.getEmp_num());
			pstmt.setString(2, photoBoard.getName());
			pstmt.setString(3, photoBoard.getTitle());
			pstmt.setString(4, photoBoard.getContent());
			pstmt.setString(5, photoBoard.getOriginFileName());
			pstmt.setString(6, photoBoard.getSaveFileName());
			pstmt.setString(7, photoBoard.getPath());
			pstmt.executeUpdate();

		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	// 테스트용 main method
	public static void main(String[] args) {
		BoardDAOOracle test = new BoardDAOOracle();
		try {
			List<NoticeBoard> list = test.selectNoticeBoard(1);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 사진게시판 조회수 업데이트
	@Override
	public void updatePBHit(String hitseq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		String updateHitSQL = "update photo_board " + "set hit = hit + 1 " + "where seq = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateHitSQL);
			pstmt.setString(1, hitseq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public int selectPBCount() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectCountSQL = "SELECT COUNT(*) totalcnt FROM photo_board";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectCountSQL);
			rs = pstmt.executeQuery();
			rs.next();
			int totalCount = rs.getInt("totalcnt");
			return totalCount;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	// 사진게시판 제목,내용만 수정
	@Override
	public void updatePhotoBoard(PhotoBoard photoBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updatePBSQL = "update photo_board \r\n" + "set title= ? , content = ? , log_time = sysdate\r\n"
				+ "where seq = ?";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updatePBSQL);
			pstmt.setString(1, photoBoard.getTitle());
			pstmt.setString(2, photoBoard.getContent());
			pstmt.setString(3, photoBoard.getSeq());
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	// 사진게시판 첨부파일까지 수정
	@Override
	public void updatePhotoBoardFile(PhotoBoard photoBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updatePBSQL = "update photo_board "
				+ "set title= ? , content =? , log_time = sysdate, originfilename = ? ,savefilename = ? , path = ?\r\n"
				+ "where seq = ?";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updatePBSQL);
			pstmt.setString(1, photoBoard.getTitle());
			pstmt.setString(2, photoBoard.getContent());
			pstmt.setString(3, photoBoard.getOriginFileName());
			pstmt.setString(4, photoBoard.getSaveFileName());
			pstmt.setString(5, photoBoard.getPath());
			pstmt.setString(6, photoBoard.getSeq());
			pstmt.executeUpdate();

		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	// 사진게시판 게시글 삭제
	@Override
	public void deletePhotoBoard(String delseq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String deletePBSQL = "delete photo_board " + "where seq = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(deletePBSQL);
			pstmt.setString(1, delseq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	// 사진게시판 이전글 가져오기.
	@Override
	public PhotoBoard selectPBPrePost(String seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectPreSQL = "select seq,emp_num,name,title,content,hit,to_char(log_time,'yyyy-mm-dd hh24:mi') log_time ,originfilename,savefilename , path\r\n"
				+ "from photo_board\r\n" + "where seq = (select pre_seq from\r\n" + "            (\r\n"
				+ "            select seq,\r\n" + "            lag(seq,1,0) over (order by seq) pre_seq "
				+ "            from photo_board\r\n" + "            )\r\n" + "            where seq= ?)";
		PhotoBoard pb = new PhotoBoard();
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectPreSQL);
			pstmt.setString(1, seq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pb.setSeq(rs.getString("seq"));
				pb.setEmp_num(rs.getString("emp_num"));
				pb.setName(rs.getString("name"));
				pb.setTitle(rs.getString("title"));
				pb.setContent(rs.getString("content"));
				pb.setHit(rs.getString("hit"));
				pb.setLog_time(rs.getString("log_time"));
				pb.setOriginFileName(rs.getString("originfilename"));
				pb.setSaveFileName(rs.getString("savefilename"));
				pb.setPath(rs.getString("path"));
			}
			return pb;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	// 사진게시판 다음글 찾기
	@Override
	public PhotoBoard selectPBNextPost(String seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectNextSQL = "select seq,emp_num,name,title,content,hit,to_char(log_time,'yyyy-mm-dd hh24:mi') log_time ,originfilename,savefilename , path\r\n"
				+ "from photo_board\r\n" + "where seq = (select next_seq from\r\n" + "            (\r\n"
				+ "            select seq,\r\n" + "            lead(seq,1,0) over (order by seq) next_seq "
				+ "            from photo_board\r\n" + "            )\r\n" + "            where seq= ?)";
		PhotoBoard pb = new PhotoBoard();
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectNextSQL);
			pstmt.setString(1, seq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pb.setSeq(rs.getString("seq"));
				pb.setEmp_num(rs.getString("emp_num"));
				pb.setName(rs.getString("name"));
				pb.setTitle(rs.getString("title"));
				pb.setContent(rs.getString("content"));
				pb.setHit(rs.getString("hit"));
				pb.setLog_time(rs.getString("log_time"));
				pb.setOriginFileName(rs.getString("originfilename"));
				pb.setSaveFileName(rs.getString("savefilename"));
				pb.setPath(rs.getString("path"));
			}
			return pb;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public int selectDeptCount(int dept_num) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectCountSQL = "SELECT COUNT(*) totalcnt FROM department_board WHERE dept_num = ?";
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(selectCountSQL);
			pstmt.setInt(1, dept_num);
			rs = pstmt.executeQuery();
			rs.next();
			int totalCount = rs.getInt("totalcnt");
			return totalCount;
		} finally {
			com.kitware.sql.MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public void insertDeptBoard(DepartBoard dpetboard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		String insertNBSQL = "INSERT INTO department_board (seq , p_seq, emp_num, name, title, dept_num, content, log_time,originfilename,savefilename,path) "
				+ "VALUES (noticeboard_seq.nextval,?,?,?,?,?,?,sysdate,?,?,?)";

		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(insertNBSQL);
			int index = 0;
			pstmt.setInt(++index, dpetboard.getP_seq());
			pstmt.setString(++index, dpetboard.getEmp_num());
			pstmt.setString(++index, dpetboard.getName());
			pstmt.setString(++index, dpetboard.getTitle());
			pstmt.setInt(++index, dpetboard.getDept_num());
			pstmt.setString(++index, dpetboard.getContent());
			pstmt.setString(++index, dpetboard.getOriginFileName());
			pstmt.setString(++index, dpetboard.getSaveFileName());
			pstmt.setString(++index, dpetboard.getPath());
			pstmt.executeUpdate();

		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public void updateDeptBoard(DepartBoard dpetboard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updateNBSQL = "update department_board \r\n" + "set title= ? , content = ? , log_time = sysdate\r\n"
				+ "where seq = ?";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateNBSQL);
			int index = 0;
			pstmt.setString(++index, dpetboard.getTitle());
			pstmt.setString(++index, dpetboard.getContent());
			pstmt.setInt(++index, dpetboard.getSeq());
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public void delDeptBoard(int seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String deleteNBSQL = "delete department_board\r\n" + "where seq = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(deleteNBSQL);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public void updateDeptHit(int seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		String updateHitSQL = "update department_board " + "set hit = hit + 1 " + "where seq = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateHitSQL);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public List<DepartBoard> getListBoard(int page, int dept_num) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectAllSQL = "SELECT  b.*" + " FROM (SELECT rownum r, level, a.*"
				+ "  from department_board a where dept_num=?" + "  start with p_seq=0" + "  connect by prior seq=p_seq"
				+ "  order siblings by seq desc)b" + " WHERE r BETWEEN ? AND ?";
		List<DepartBoard> list = new ArrayList<>();
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(selectAllSQL);
			int cntPerPage = 7;// 1페이지별 3건씩 보여준다
			int endRow = cntPerPage * page;
			int startRow = endRow - cntPerPage + 1;
			System.out.println("startRow:" + startRow);
			System.out.println("endRow:" + endRow);
			int index = 0;
			pstmt.setInt(++index, dept_num);
			pstmt.setInt(++index, startRow);
			pstmt.setInt(++index, endRow);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DepartBoard db = new DepartBoard();
				db.setNum(rs.getString("r"));
				db.setSeq(rs.getInt("seq"));
				db.setLevel(rs.getInt("level"));
				db.setEmp_num(rs.getString("emp_num"));
				db.setTitle(rs.getString("title"));
				db.setName(rs.getString("name"));
				db.setHit(rs.getInt("hit"));
				db.setLog_time(rs.getString("log_time"));
				db.setContent(rs.getString("content"));
				db.setOriginFileName(rs.getString("originfilename"));
				db.setSaveFileName(rs.getString("savefilename"));
				db.setPath(rs.getString("path"));
				db.setComment(countcommnet(rs.getInt("seq")));
				list.add(db);
			}
			return list;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public NoticeBoard selectPreDeptPost(String seq, int dept_num) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoticeBoard selectNextDeptPost(String seq, int dept_num) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getCommentList(int seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectAllCmt = "select c_seq,name,emp_num, to_char(log_time,'yy-mm-dd hh24:mi') log_time, content from d_board_comment where p_seq=? order by c_seq";
		List<Comment> list = new ArrayList<>();
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(selectAllCmt);
			int index = 0;
			pstmt.setInt(++index, seq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Comment cm = new Comment();
				cm.setC_seq(rs.getInt("c_seq"));
				cm.setEmp_num(rs.getString("emp_num"));
				cm.setName(rs.getString("name"));
				cm.setLog_time(rs.getString("log_time"));
				cm.setContent(rs.getString("content"));
				list.add(cm);
			}
			return list;
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public void insertComment(int seq, Members m, String content) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;

		String writeCmtSQL = "insert into d_board_comment(c_seq,p_seq,emp_num,name,dept_num,content,log_time) values(d_comment_seq.nextval,?,?,?,?,?,sysdate)";
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(writeCmtSQL);
			int index = 0;
			pstmt.setInt(++index, seq);
			pstmt.setString(++index, m.getEmp_num());
			pstmt.setString(++index, m.getName());
			pstmt.setString(++index, m.getDept_num());
			pstmt.setString(++index, content);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public void delComment(int seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String writeCmtSQL = "delete from d_board_comment where c_seq=?";
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(writeCmtSQL);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public int countcommnet(int seq) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String writeCmtSQL = "select count(*) c from d_board_comment where p_seq=?";
		try {
			con = com.kitware.sql.MyConnection.getConnection();
			pstmt = con.prepareStatement(writeCmtSQL);
			pstmt.setInt(1, seq);
			rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt("c");
		} finally {
			MyConnection.close(pstmt, con);
		}

	}

	@Override
	public void updateDeptBoardFile(DepartBoard deptBoard) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updateNBSQL = "update department_board\r\n"
				+ "set title= ? , content =? , log_time = sysdate, originfilename = ? ,savefilename = ? , path = ?\r\n"
				+ "where seq = ?";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(updateNBSQL);
			pstmt.setString(1, deptBoard.getTitle());
			pstmt.setString(2, deptBoard.getContent());
			pstmt.setString(3, deptBoard.getOriginFileName());
			pstmt.setString(4, deptBoard.getSaveFileName());
			pstmt.setString(5, deptBoard.getPath());
			pstmt.setInt(6, deptBoard.getSeq());
			pstmt.executeUpdate();

		} finally {
			MyConnection.close(pstmt, con);
		}		
	}

}
