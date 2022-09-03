package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PaymentMapper {

    @Mapping(target = "courseId", expression = "java(payment.getCourse().getId())")
    @Mapping(target = "lessonId", expression = "java(payment.getLesson().getId())")
    @Mapping(target = "numberCardUser", expression = "java(payment.getCostumer().getNumberCard())")
    @Mapping(target = "emailUser", expression = "java(payment.getCostumer().getEmail())")
    public PaymentDto paymentToPaymentDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setId(payment.getId());
        paymentDto.setDatePayment(payment.getDatePayment());
        paymentDto.setPrice(payment.getPrice());
        if (payment.getCourse() != null) {
            paymentDto.setCourseId(payment.getCourse().getId());

        } else {
            paymentDto.setLessonId(payment.getLesson().getId());
        }
        paymentDto.setNumberCardUser(payment.getCostumer().getNumberCard());
        paymentDto.setEmailUser(payment.getCostumer().getEmail());
        return paymentDto;
    }

    @Mapping(target = "costumer", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    public abstract Payment paymentDtoToPayment(PaymentDto paymentDto);

    public abstract List<PaymentDto> toPaymentDto(Collection<Payment> payments);
}
