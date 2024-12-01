package com.blogging.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogging.app.payloads.ApiResponse;
import com.blogging.app.payloads.CommentDto;
import com.blogging.app.services.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/comment")
@Tag(name = "Comment APIs")
public class CommentController {
	
	@Autowired
	private CommentService commentService;

	@PostMapping("/createComment/{postId}")
	public ResponseEntity<CommentDto> createComments(@RequestBody CommentDto commentDto, @PathVariable("postId") Integer postId){
	CommentDto createdComment = this.commentService.createComment(commentDto, postId);
	return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/deleteComment/{id}")
 	public ResponseEntity<ApiResponse> deleteComment(@PathVariable("id") Integer commentId){
 		this.commentService.deleteComment(commentId);
 		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted succesfully...", true), HttpStatus.OK);
 	}

}
