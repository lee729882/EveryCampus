package com.everycampus.controller;

import com.everycampus.entity.Vote;
import com.everycampus.entity.VoteOption;
import com.everycampus.entity.VoteRecord;
import com.everycampus.repository.VoteOptionRepository;
import com.everycampus.repository.VoteRecordRepository;
import com.everycampus.repository.VoteRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteController {

    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;

    // ✅ 투표 목록 보기
    @GetMapping
    public String voteList(Model model) {
        System.out.println("🔥 /vote 진입"); // 이게 찍히는지 확인
        model.addAttribute("votes", voteRepository.findAll());
        return "vote/list";
    }
    // ✅ 투표 상세 보기 및 투표하기
    @GetMapping("/{id}")
    public String voteForm(@PathVariable Long id, Model model) {
        Vote vote = voteRepository.findById(id).orElseThrow();
        model.addAttribute("vote", vote);
        return "vote/form";
    }

    // ✅ 투표 처리
    @PostMapping("/{id}/submit")
    public String submitVote(@PathVariable Long id,
                             @RequestParam Long optionId,
                             @RequestParam String username) {

        if (voteRecordRepository.existsByUsernameAndVoteId(username, id)) {
            return "redirect:/vote/" + id + "?error=alreadyVoted";
        }

        Vote vote = voteRepository.findById(id).orElseThrow();
        VoteOption selected = voteOptionRepository.findById(optionId).orElseThrow();

        // 카운트 증가
        selected.setCount(selected.getCount() + 1);
        voteOptionRepository.save(selected);

        // 기록 저장
        VoteRecord record = new VoteRecord();
        record.setUsername(username);
        record.setVote(vote);
        record.setSelectedOption(selected);
        voteRecordRepository.save(record);

        return "redirect:/vote/" + id + "/result";
    }
    
    @PostMapping("/create")
    public String submitCreate(@RequestParam String title,
                               @RequestParam("options") List<String> options,
                               HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            return "redirect:/login"; // 로그인하지 않은 경우
        }

        if (options.size() < 2 || options.size() > 5) {
            return "redirect:/vote/create?error=invalidOptionCount";
        }

        Vote vote = new Vote();
        vote.setTitle(title);

        List<VoteOption> optionList = new ArrayList<>();
        for (String opt : options) {
            VoteOption o = new VoteOption();
            o.setContent(opt);
            o.setVote(vote);
            optionList.add(o);
        }

        vote.setOptions(optionList);
        voteRepository.save(vote);

        return "redirect:/vote"; // 저장 후 목록으로 이동
    }
    
 // ✅ 새 투표 폼 화면 추가
    @GetMapping("/create")
    public String createForm() {
        return "vote/create";
    }
    
    // ✅ 결과 보기
    @GetMapping("/{id}/result")
    public String voteResult(@PathVariable Long id, Model model) {
        Vote vote = voteRepository.findById(id).orElseThrow();
        model.addAttribute("vote", vote);
        return "vote/result";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteVote(@PathVariable Long id) {
        Vote vote = voteRepository.findById(id).orElseThrow();

        // 🔥 수동으로 연관 레코드 제거 (안정성 확보)
        voteRecordRepository.deleteAll(vote.getRecords());
        voteOptionRepository.deleteAll(vote.getOptions());

        voteRepository.delete(vote);
        return "redirect:/vote";
    }
}
