package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.AppUser;

/**
 * App用户dao ClassName: AppUserDao
 *
 * @author HD
 * @Description: TODO
 * @date 2018年4月10日
 */
@Transactional
@Repository
public interface AppUserDao extends CrudRepository<AppUser, Integer> {
    // 根据app用户的昵称查询信息
    @Query("from AppUser where userName like :userName and state='0'")
    public List<AppUser> findByUserName(@Param("userName") String userName);

    // 根据id查询信息
    public AppUser findById(Long id);

    // 根据openId查询信息
    public AppUser findByOpenId(String id);

    // 查询所有的APPuser
    Page<AppUser> findAll(Pageable pageable);

    // 模糊查询用户
    @Query("from AppUser where userName like :search or phone like :search")
    List<AppUser> findAll(@Param("search") String search, Pageable pageable);

    @Query("from AppUser where userName like :search or phone like :search")
    List<AppUser> findAll(@Param("search") String search);

    // 查询所有的APPuser
    List<AppUser> findAll();

    // APP登录
    AppUser findByPhoneAndPhonePwdAndPrefix(String Phone, String PhonePwd, String prefix);

    AppUser findByPhoneAndPhonePwd(String Phone, String PhonePwd);

    // 检查是否注册
    AppUser findByPhoneAndPrefix(String phone, String prefix);

    // 检查是否注册
    AppUser findByPhone(String phone);

    // 根据用户id获取所关注的用户的信息
    @Query(nativeQuery = true, value = "select * from app_user where id in  (select be_followed_id from attention where follower_id = :followerId and judge = 0) limit :page,:limit ")
    public List<AppUser> getColContentByFollowerId(@Param("followerId") Long followerId, @Param("page") Integer page,
                                                   @Param("limit") Integer limit);

    // 根据用户id获取关注自己的用户的信息
    @Query(nativeQuery = true, value = "select * from app_user where id in  (select follower_id from attention where be_followed_id = :beFollowedId) limit :page,:limit")
    public List<AppUser> getColContentByBeFollowerId(@Param("beFollowedId") Long beFollowedId,
                                                     @Param("page") Integer page, @Param("limit") Integer limit);
    // 根据用户id获取关注自己的用户同时自己关注的用户的信息

    // 根据用户id和好友分组id查询数据
    @Query(nativeQuery = true, value = "select * from app_user where id in (select friend_id from friend where appUser_id=:appUserId and friend_grop_id =:friendGropId and is_pass=:isPass)")
    public List<AppUser> getAppUserByFriend(@Param("appUserId") Long appUserId,
                                            @Param("friendGropId") Long friendGropId, @Param("isPass") Integer isPass);

    // 根据好友id和状态获取数据
    @Query(nativeQuery = true, value = "select distinct a.id,a.is_pc,a.phone,a.slt_img,a.state,a.sy_img1,a.sy_img2,a.sy_img3,a.user_img,a.user_name,a.prefix,a.open_id,a.phone_pwd,f.is_pass from app_user  a inner join friend f on a.id = f.app_user_id where a.id in (select app_user_id from friend where friend_id=:friendId and (is_pass=1 or is_pass=3))")
    public List<AppUser> getAppUserByIsPass(@Param("friendId") Long friendId);

    // 修改所有的左上角水印syImg1
    @Query(nativeQuery = true, value = "update app_user set sy_img1 =:syImg1 ")
    @Modifying
    public void updateAppUserSyImg1(@Param("syImg1") String syImg1);

    AppUser findByBid(Long bid);
}
