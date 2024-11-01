package com.blogging.app.services;

import com.blogging.app.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto,Integer postId);
	
	CommentDto updateComment(CommentDto commentDto,Integer postId, Integer commentId);
	
	void deleteComment(Integer commentId);
	
}
