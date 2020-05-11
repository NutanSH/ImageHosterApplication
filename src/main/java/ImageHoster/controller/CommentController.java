package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments", method = RequestMethod.POST)
    public String addComments(@PathVariable("imageId") int id, @PathVariable("imageTitle") String imageTitle, @RequestParam("comment") String text, Comment comment, HttpSession session, Model model) throws IOException {

        User user = (User) session.getAttribute("loggeduser");
        Image image = imageService.getImageByTitle(imageTitle, id);

        comment.setUser(user);
        comment.setImage(image);
        comment.setText(text);
        comment.setCreatedDate(new Date());
        commentService.addComment(comment);
        
        return "redirect:/images/" + id + "/" + imageTitle;
    }

    //This method displays all the images in the user home page after successful login

    @RequestMapping("/image/{imageId}/{imageTitle}/comments")
    public String getComments(@PathVariable("imageId") int id, Model model) {
        List<Comment> comments = commentService.fetchComments(id);
        model.addAttribute("comments", comments);
        return "images/image";
    }

}
