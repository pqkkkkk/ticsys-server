package com.example.ticsys.app.outbound.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LinkBankAccountEvent {
    String idOnTicsys;
    String bankAccountId;
    String bankAccountOwnerName;
}
