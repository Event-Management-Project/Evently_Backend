package com.project.service;

import com.project.dto.ApiResponse;
import com.project.dto.PaymentDTO;

public interface PaymentService {

	ApiResponse makePayment(PaymentDTO paymentDto,long bkg_id);

}