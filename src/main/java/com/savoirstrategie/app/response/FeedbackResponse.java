package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Etablissement;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    private LocalDateTime feedbackDate;


    private String message;

    private String etablissementName;
}
