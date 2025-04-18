package br.com.zippydeliveryapi.service;

import java.time.LocalDate;
 import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import br.com.zippydeliveryapi.model.PromoCode;
import br.com.zippydeliveryapi.model.dto.request.PromoCodeRequest;
import br.com.zippydeliveryapi.repository.PromoCodeRepository;
import br.com.zippydeliveryapi.util.exception.PromoCodeException;
import jakarta.transaction.Transactional;

@Service
public class PromoCodeService {

    @Autowired
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Transactional
    public PromoCode save(PromoCodeRequest request) {
        Optional<PromoCode> existingPromoCode = this.promoCodeRepository.findByCode(request.getCode());
        if (existingPromoCode.isPresent()) {
            throw new PromoCodeException("An active promo code with this code already exists.");
        }
        PromoCode promoCode = PromoCode.fromRequest(request);
        this.validateDateRange(promoCode);
        promoCode.setEnabled(Boolean.TRUE);
        return this.promoCodeRepository.save(promoCode);
    }

    public List<PromoCode> findAll() {
        return this.promoCodeRepository.findAll();
    }

    public PromoCode findById(Long id) {
        return this.promoCodeRepository.findById(id)
                .orElseThrow(() -> new PromoCodeException(String.format("Promo code with id %s not found.", id)));
    }

    @Transactional
    public PromoCode findByCode(String code) {
        return this.promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new PromoCodeException(String.format("Promo code with code %s not found.", code)));
    }

    @Transactional
    public void update(Long id, PromoCodeRequest request) {
        PromoCode promoCode = this.findById(id);
        promoCode.setCode(StringUtils.hasText(request.getCode()) ? request.getCode() : promoCode.getCode());
        promoCode.setDiscountPercentage((request.getDiscountPercentage() > 0) ? request.getDiscountPercentage() : promoCode.getDiscountPercentage());
        promoCode.setDiscountValue((request.getDiscountValue() >= 0) ? request.getDiscountValue() : promoCode.getDiscountValue());
        promoCode.setMinAllowedOrderValue((request.getMinOrderValue() > 0) ? request.getMinOrderValue() : promoCode.getMinAllowedOrderValue());
        promoCode.setMaxUsageQuantity((request.getMaxUsageQuantity() > 0) ? request.getMaxUsageQuantity() : promoCode.getMaxUsageQuantity());
        promoCode.setStartDate(request.getStartDate());
        promoCode.setEndDate(request.getEndDate());
        this.promoCodeRepository.save(promoCode);
    }

    @Transactional
    public void delete(Long id) {
        PromoCode promoCode = this.findById(id);
        promoCode.setEnabled(Boolean.FALSE);
        promoCode.setCode("");
        this.promoCodeRepository.save(promoCode);
    }

    private void validateDateRange(PromoCode promoCode) {
        Objects.requireNonNull(promoCode, "Promo code cannot be null.");
        if (!promoCode.getEndDate().isAfter(promoCode.getStartDate())) {
            throw new PromoCodeException(PromoCodeException.MESSAGE_INVALID_DATE);
        }
    }

    public boolean validateCoupon(PromoCode promoCode) {
        LocalDate today = LocalDate.now();
        LocalDate start = promoCode.getStartDate();
        LocalDate end = promoCode.getEndDate();
        return (today.isEqual(start) || today.isAfter(start)) && (today.isEqual(end) || today.isBefore(end));
    }

    public Double applyCoupon(Double totalAmount, PromoCode promoCode) {
        Double discount = 0.0;

        if (promoCode.getDiscountPercentage() != null && promoCode.getDiscountPercentage() != 0.0) {
            discount = totalAmount * (promoCode.getDiscountPercentage() / 100);
        } else if (promoCode.getDiscountValue() != null && promoCode.getDiscountValue() != 0.0) {
            discount = promoCode.getDiscountValue();
        }

        promoCode.setMaxUsageQuantity(promoCode.getMaxUsageQuantity() - 1);
        this.update(promoCode.getId(), PromoCodeRequest.fromEntity(promoCode));
        return totalAmount - discount;
    }
}