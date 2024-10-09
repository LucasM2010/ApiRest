package com.example.projetoapi.controller;

import com.example.projetoapi.model.Comment;
import com.example.projetoapi.model.Post;
import com.example.projetoapi.model.User;
import com.example.projetoapi.repository.CommentRepository;
import com.example.projetoapi.repository.PostRepository;
import com.example.projetoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        User user = userRepository.findById(comment.getUser().getId()).orElse(null);
        Post post = postRepository.findById(comment.getPost().getId()).orElse(null);
        if (user == null || post == null) {
            return ResponseEntity.badRequest().build();
        }
        comment.setUser(user);
        comment.setPost(post);
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment commentDetails) {
        return commentRepository.findById(id)
                .map(comment -> {
                    comment.setText(commentDetails.getText());
                    return ResponseEntity.ok(commentRepository.save(comment));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
