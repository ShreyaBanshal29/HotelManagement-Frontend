package com.example.Frontend.Controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Frontend.Entities.Reservation;
import com.example.Frontend.Entities.Room;
import com.example.Frontend.Service.ReservationService;
import com.example.Frontend.dtos.PageInfo;
import com.example.Frontend.dtos.ReservationFormDto;
import com.example.Frontend.dtos.ReservationResponse;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @GetMapping
    public String reservations(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String filterBy,
                               @RequestParam(required = false) String keyword,
                               Model model) {
        ReservationResponse response = service.getReservations(page, filterBy, keyword);

        List<Reservation> reservations = response != null ? response.getContent() : Collections.emptyList();
        PageInfo pageInfo = response != null ? response.getPage() : null;

        model.addAttribute("reservations", reservations);
        model.addAttribute("currentPage", pageInfo != null ? pageInfo.getNumber() : page);
        model.addAttribute("totalPages", pageInfo != null ? pageInfo.getTotalPages() : 0);
        model.addAttribute("filterBy", filterBy != null ? filterBy : "guestName");
        model.addAttribute("keyword", keyword);

        return "reservations";
    }

    @GetMapping("/new")
    public String newReservation(Model model) {
        model.addAttribute("reservationForm", new ReservationFormDto());
        model.addAttribute("rooms", service.getRooms());
        model.addAttribute("mode", "create");
        model.addAttribute("pageTitle", "New Reservation");
        model.addAttribute("today", LocalDate.now());
        return "reservation-form";
    }

    @PostMapping
    public String createReservation(@ModelAttribute("reservationForm") ReservationFormDto form,
                                    RedirectAttributes redirectAttributes) {
        try {
            service.createReservation(form);
            redirectAttributes.addFlashAttribute("success", "Reservation created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", readableError(e));
            return "redirect:/reservations/new";
        }
        return "redirect:/reservations";
    }

    @GetMapping("/{id}/edit")
    public String editReservation(@PathVariable Integer id, Model model) {
        Reservation reservation = service.getReservationById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("reservationForm", service.toForm(reservation));
        model.addAttribute("mode", "edit");
        model.addAttribute("pageTitle", "Edit Reservation");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("formAction", "/reservations/" + id);
        return "reservation-form";
    }

    @PostMapping("/{id}")
    public String updateReservation(@PathVariable Integer id,
                                    @ModelAttribute("reservationForm") ReservationFormDto form,
                                    RedirectAttributes redirectAttributes) {
        try {
            service.updateReservation(id, form);
            redirectAttributes.addFlashAttribute("success", "Reservation updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", readableError(e));
            return "redirect:/reservations/" + id + "/edit";
        }
        return "redirect:/reservations";
    }

    @GetMapping("/{id}")
    public String reservationDetail(@PathVariable Integer id, Model model) {
        Reservation reservation = service.getReservationById(id);
        Room room = reservation != null ? reservation.getRoom() : null;

        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        model.addAttribute("hotel", room != null ? room.getHotel() : null);
        model.addAttribute("roomType", room != null ? room.getRoomType() : null);

        return "reservation-detail";
    }

    private String readableError(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isBlank()) {
            return "Reservation could not be saved. Please check the values and try again.";
        }
        return "Reservation could not be saved. Please check the values and try again.";
    }
}
