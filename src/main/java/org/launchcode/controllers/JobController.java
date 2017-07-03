package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        //get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
//        JobFieldType[] fields = JobFieldType.values();
        model.addAttribute("job", job);
//        model.addAttribute("fields", fields);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        if (errors.hasErrors()) {

            return "new-job";
        }

        String name = jobForm.getName();
        Employer employer = jobData.findById(jobForm.getEmployerId()).getEmployer();
        Location location = jobData.findById(Integer.parseInt(jobForm.getLocation().getValue())).getLocation();
        PositionType positionType = jobData.findById(Integer.parseInt(jobForm.getPositionType().getValue())).getPositionType();
        CoreCompetency skill = jobData.findById(Integer.parseInt(jobForm.getCoreCompetency().getValue())).getCoreCompetency();

        Job job = new Job(name, employer, location, positionType, skill);

        jobData.add(job);

        model.addAttribute("job", job);

        return "redirect:/job/?id=" +job.getId();

    }
}
