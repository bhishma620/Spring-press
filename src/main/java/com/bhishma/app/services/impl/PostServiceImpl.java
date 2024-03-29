package com.bhishma.app.services.impl;

import com.bhishma.app.entities.Category;
import com.bhishma.app.entities.Post;
import com.bhishma.app.entities.User;
import com.bhishma.app.exceptions.ResourceNotFoundException;
import com.bhishma.app.payloads.CategoryDto;
import com.bhishma.app.payloads.PostDto;
import com.bhishma.app.payloads.PostResponse;
import com.bhishma.app.repositories.CategoryRepo;
import com.bhishma.app.repositories.PostRepo;
import com.bhishma.app.repositories.UserRepo;
import com.bhishma.app.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

        User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ","user id",userId));

        Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ","Category id",categoryId));

        Post post=this.modelMapper.map(postDto,Post.class);
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post createdPost= this.postRepo.save(post);
        return this.modelMapper.map(createdPost,PostDto.class);

    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

     Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post ","post id",postId));

     post.setTitle(postDto.getTitle());
     post.setContent(postDto.getContent());

    Post updatedPost=  this.postRepo.save(post);

    return this.modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post ","post id",postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize,String sortBY,String sortDir) {

        Sort sort=null;
        if(sortDir.equalsIgnoreCase("dsc")){
            sort=Sort.by(sortBY).descending();
        }
        else {
            sort=Sort.by(sortBY).ascending();
        }
        Pageable p= PageRequest.of(pageNumber,pageSize,sort);

        Page<Post> pagePost=this.postRepo.findAll(p);

        List<Post>allPosts=pagePost.getContent();

        List<PostDto>postDtos=allPosts.stream().map(post->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();

        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElments(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {

        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post ","post id",postId));

        PostDto postdto=this.modelMapper.map(post,PostDto.class);

        return postdto;

    }


    @Override
    public List<PostDto> getPostByCategory(Integer categoryId) {
        Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ","category id",categoryId));

     List<Post>posts=   this.postRepo.findByCategory(category);

     List<PostDto>postDtos=   posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getPostByUser(Integer userId) {

        User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ","user id",userId));

        List<Post>posts=   this.postRepo.findByUser(user);

        List<PostDto>postDtos=   posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {

       List<Post>posts= this.postRepo.findByTitleContaining(keyword);

       List<PostDto>postDtos=posts.stream().map(post->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

       return postDtos;

    }

}
