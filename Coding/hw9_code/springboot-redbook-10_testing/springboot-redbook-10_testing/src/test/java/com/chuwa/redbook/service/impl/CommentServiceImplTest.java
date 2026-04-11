package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for {@link CommentServiceImpl} — repositories and {@link ModelMapper} are mocked
 * (same style as {@link PostServiceImplTest}).
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post post;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        post = new Post(1L, "title", "desc", "content", LocalDateTime.now(), LocalDateTime.now());

        comment = new Comment(10L, "Alice", "alice@example.com", "Hello world comment body");
        comment.setPost(post);

        commentDto = new CommentDto(10L, "Alice", "alice@example.com", "Hello world comment body");
    }

    @Test
    void createComment_success() {
        Comment mappedFromDto = new Comment();
        Mockito.when(modelMapper.map(ArgumentMatchers.any(CommentDto.class), ArgumentMatchers.eq(Comment.class)))
                .thenReturn(mappedFromDto);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenReturn(comment);
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class)))
                .thenReturn(commentDto);

        CommentDto result = commentService.createComment(1L, commentDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(commentDto.getName(), result.getName());
        Assertions.assertEquals(commentDto.getEmail(), result.getEmail());
        Assertions.assertEquals(commentDto.getBody(), result.getBody());
        Assertions.assertSame(post, mappedFromDto.getPost());
        Mockito.verify(commentRepository, Mockito.times(1)).save(mappedFromDto);
    }

    @Test
    void createComment_postNotFound_throwsResourceNotFoundException() {
        Comment mappedFromDto = new Comment();
        Mockito.when(modelMapper.map(ArgumentMatchers.any(CommentDto.class), ArgumentMatchers.eq(Comment.class)))
                .thenReturn(mappedFromDto);
        Mockito.when(postRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> commentService.createComment(99L, commentDto));

        Assertions.assertEquals("Post", ex.getResourceName());
        Assertions.assertEquals("id", ex.getFieldName());
        Assertions.assertEquals(99L, ex.getFieldValue());
        Mockito.verify(commentRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void getCommentsByPostId_emptyList() {
        Mockito.when(commentRepository.findByPostId(1L)).thenReturn(Collections.emptyList());

        List<CommentDto> result = commentService.getCommentsByPostId(1L);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(modelMapper, Mockito.never()).map(
                ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class));
    }

    @Test
    void getCommentsByPostId_multipleComments() {
        Comment c1 = new Comment(1L, "A", "a@x.com", "body one here");
        c1.setPost(post);
        Comment c2 = new Comment(2L, "B", "b@x.com", "body two here");
        c2.setPost(post);
        Mockito.when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(c1, c2));

        CommentDto dto1 = new CommentDto(1L, "A", "a@x.com", "body one here");
        CommentDto dto2 = new CommentDto(2L, "B", "b@x.com", "body two here");
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class)))
                .thenAnswer(invocation -> {
                    Comment c = invocation.getArgument(0);
                    if (c.getId() == 1L) {
                        return dto1;
                    }
                    return dto2;
                });

        List<CommentDto> result = commentService.getCommentsByPostId(1L);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(dto1, result.get(0));
        Assertions.assertEquals(dto2, result.get(1));
    }

    @Test
    void getCommentById_success() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        Mockito.when(modelMapper.map(ArgumentMatchers.same(comment), ArgumentMatchers.eq(CommentDto.class)))
                .thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(1L, 10L);

        Assertions.assertSame(commentDto, result);
    }

    @Test
    void getCommentById_postNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).findById(ArgumentMatchers.any());
    }

    @Test
    void getCommentById_commentNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 999L));
    }

    @Test
    void getCommentById_commentDoesNotBelongToPost_throwsBlogAPIException() {
        Post otherPost = new Post(2L, "other", "d", "c", LocalDateTime.now(), LocalDateTime.now());
        comment.setPost(otherPost);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = Assertions.assertThrows(BlogAPIException.class,
                () -> commentService.getCommentById(1L, 10L));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        Assertions.assertEquals("Comment does not belong to post", ex.getMessage());
    }

    @Test
    void updateComment_success_updatesFieldsAndReturnsDto() {
        CommentDto request = new CommentDto(0L, "Bob", "bob@example.com", "Updated body text here");

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(modelMapper.map(ArgumentMatchers.same(comment), ArgumentMatchers.eq(CommentDto.class)))
                .thenReturn(new CommentDto(10L, "Bob", "bob@example.com", "Updated body text here"));

        CommentDto result = commentService.updateComment(1L, 10L, request);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        Mockito.verify(commentRepository).save(captor.capture());
        Comment saved = captor.getValue();
        Assertions.assertEquals("Bob", saved.getName());
        Assertions.assertEquals("bob@example.com", saved.getEmail());
        Assertions.assertEquals("Updated body text here", saved.getBody());
        Assertions.assertEquals("Bob", result.getName());
        Assertions.assertEquals("bob@example.com", result.getEmail());
    }

    @Test
    void updateComment_postNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));
        Mockito.verify(commentRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void updateComment_commentNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));
        Mockito.verify(commentRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void updateComment_wrongPost_throwsBlogAPIException() {
        Post otherPost = new Post(2L, "o", "d", "c", LocalDateTime.now(), LocalDateTime.now());
        comment.setPost(otherPost);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = Assertions.assertThrows(BlogAPIException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        Mockito.verify(commentRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void deleteComment_success() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).delete(ArgumentMatchers.same(comment));

        commentService.deleteComment(1L, 10L);

        Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);
    }

    @Test
    void deleteComment_postNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    void deleteComment_commentNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    void deleteComment_wrongPost_doesNotDelete() {
        Post otherPost = new Post(2L, "o", "d", "c", LocalDateTime.now(), LocalDateTime.now());
        comment.setPost(otherPost);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        Assertions.assertThrows(BlogAPIException.class,
                () -> commentService.deleteComment(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    void commentServiceMapperUtil_mapsWithRealModelMapper() {
        Comment c = new Comment(5L, "UtilUser", "u@example.com", "Utility mapper body text");

        CommentDto dto = CommentServiceImpl.commentServiceMapperUtil(c);

        Assertions.assertEquals(5L, dto.getId());
        Assertions.assertEquals("UtilUser", dto.getName());
        Assertions.assertEquals("u@example.com", dto.getEmail());
        Assertions.assertEquals("Utility mapper body text", dto.getBody());
    }
}
