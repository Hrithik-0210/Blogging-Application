package com.blogging.app.servicesImpl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blogging.app.entities.Category;
import com.blogging.app.entities.Post;
import com.blogging.app.entities.User;
import com.blogging.app.exception.ResourceNotFoundException;
import com.blogging.app.payloads.PostDto;
import com.blogging.app.payloads.PostResponseDto;
import com.blogging.app.repositories.CategoryRepo;
import com.blogging.app.repositories.PostRepo;
import com.blogging.app.repositories.UserRepo;
import com.blogging.app.services.PostService;


@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public PostDto createPost(Integer userId, Integer categoryId, PostDto postDto, MultipartFile imageFile) throws IOException {
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "id", categoryId));
		Post post  = this.modelMapper.map(postDto, Post.class);
		if(imageFile!=null && !imageFile.isEmpty()) {
			post.setImageName(imageFile.getOriginalFilename());
			post.setImageData(imageFile.getBytes());
		}
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		Post savedPost = this.postRepo.save(post);
		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
			post.setTitle(postDto.getTitle());
			post.setContent(postDto.getContent());
			this.postRepo.save(post);
		return this.modelMapper.map(post, PostDto.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponseDto getAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); 
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Page<Post> posts = this.postRepo.findAll(pageable);
		List<PostDto> postDto =  posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setContent(postDto);
		postResponseDto.setPageNo(posts.getNumber());
		postResponseDto.setPageSize(posts.getSize());
		postResponseDto.setTotalElement(posts.getTotalElements());
		postResponseDto.setTotalPages(posts.getTotalPages());
		postResponseDto.setLastPage(posts.isLast());
		return postResponseDto;
	}

	@Override
	public PostResponseDto getPostByCategory(Integer categoryId,  int pageNo, int pageSize, String sortBy, String sortDir ) {
		Sort sort = (sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); 
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "id", categoryId));
		Page<Post> posts =  this.postRepo.findByCategory(category, pageable);	
		List<PostDto> postDto = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setContent(postDto);
		postResponseDto.setPageNo(posts.getNumber());
		postResponseDto.setPageSize(posts.getSize());
		postResponseDto.setTotalPages(posts.getTotalPages());
		postResponseDto.setTotalElement(posts.getTotalElements());
		postResponseDto.setLastPage(posts.isLast());
		return postResponseDto ;
	}

	@Override
	public PostResponseDto getPostByUser(Integer userId, int pageNo, int pageSize, String sortBy, String sortDir  ) {
		Sort sort = (sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); 
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
		Page<Post> posts = 	this.postRepo.findByUser(user,pageable);
		List<PostDto> postDto = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setContent(postDto);
		postResponseDto.setPageNo(posts.getNumber());
		postResponseDto.setPageSize(posts.getSize());
		postResponseDto.setTotalPages(posts.getTotalPages());
		postResponseDto.setTotalElement(posts.getTotalElements());
		postResponseDto.setLastPage(posts.isLast());
		return postResponseDto;
	}

	//search by keyword
	@Override
	public PostResponseDto searchPost(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending(); 
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		 Page<Post> posts =  this.postRepo.findByKeyword(keyword, pageable);
		List<PostDto> postDto =  posts.stream().map(post-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setContent(postDto);
		postResponseDto.setPageNo(posts.getNumber());
		postResponseDto.setPageSize(posts.getSize());
		postResponseDto.setTotalPages(posts.getTotalPages());
		postResponseDto.setTotalElement(posts.getTotalElements());
		postResponseDto.setLastPage(posts.isLast());
		return postResponseDto;
	}

	
}
