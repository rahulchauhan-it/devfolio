package com.rahulchauhan.devfolio.project;

import java.time.LocalDate;
import java.util.List;

public record ProjectSummaryResponse(String slug, String title, String summary, String coverImageUrl, boolean featured,
                                     LocalDate startedOn, List<String> skills) {
}
