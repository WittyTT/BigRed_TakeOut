package TT.Service.Impl;

import TT.Mapper.AddressBookMapper;
import TT.Service.AddressBookService;
import TT.entity.AddressBook;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @description:
 * @date 2022/12/14 22:56
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
