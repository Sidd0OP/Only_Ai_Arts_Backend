CREATE OR REPLACE FUNCTION public.get_post_latest(IN offset_value bigint,IN logged_in_user_id bigint)
    RETURNS TABLE(post_id bigint, title character varying, body text, created timestamp with time zone, edited timestamp with time zone, comment_count integer, image_url character varying, heart integer, model character varying, rated boolean, user_id bigint, name character varying, profile_photo_url character varying, tags text, hearted boolean)
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100    ROWS 1000 
    
AS $BODY$

BEGIN
  
    

    RETURN QUERY
    select 
			p.id as post_id,
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags , 
			exists (select 1 from heart h where h.post_id = p.id and h.user_id = logged_in_user_id) as hearted
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id 
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url
			order by p.created desc 
			limit 20
			offset offset_value;
END;
$BODY$;






CREATE OR REPLACE FUNCTION public.get_post_of_id(IN post_of_id bigint,IN logged_in_user_id bigint)
    RETURNS TABLE(post_id bigint, title character varying, body text, created timestamp with time zone, edited timestamp with time zone, comment_count integer, image_url character varying, heart integer, model character varying, rated boolean, user_id bigint, name character varying, profile_photo_url character varying, tags text, hearted boolean)
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100    ROWS 1000 
    
AS $BODY$

BEGIN
  
    

    RETURN QUERY
    select 
			p.id as post_id,
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags,
			exists (select 1 from heart h where h.post_id = p.id and h.user_id = logged_in_user_id) as hearted
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id
			WHERE 
			
			p.id = post_of_id 

			Group by 

			p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url;

			
			
			
END;
$BODY$;













CREATE OR REPLACE FUNCTION public.get_post_of_tag(IN offset_value bigint,IN tag_query text,IN logged_in_user_id bigint)
    RETURNS TABLE(post_id bigint, title character varying, body text, created timestamp with time zone, edited timestamp with time zone, comment_count integer, image_url character varying, heart integer, model character varying, rated boolean, user_id bigint, name character varying, profile_photo_url character varying, tags text, hearted boolean)
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100    ROWS 1000 
    
AS $BODY$

BEGIN
  
    

    RETURN QUERY
    select 
			p.id as post_id, 
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags,
			exists (select 1 from heart h where h.post_id = p.id and h.user_id = logged_in_user_id) as hearted
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id
			WHERE 
			
			EXISTS (
			    SELECT 1 FROM tag t2 WHERE t2.post_id = p.id AND t2.text ILIKE tag_query
			)
			
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url 
			order by p.heart desc , p.created desc , p.comment_count desc
			limit 20
			offset offset_value;
END;
$BODY$;











CREATE OR REPLACE FUNCTION public.get_post_of_user(IN offset_value bigint,IN user_id_of_post bigint,IN logged_in_user_id bigint)
    RETURNS TABLE(post_id bigint, title character varying, body text, created timestamp with time zone, edited timestamp with time zone, comment_count integer, image_url character varying, heart integer, model character varying, rated boolean, user_id bigint, name character varying, profile_photo_url character varying, tags text, hearted boolean)
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100    ROWS 1000 
    
AS $BODY$

BEGIN
  
    

    RETURN QUERY
    select 
			p.id as post_id, 
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags,
			exists (select 1 from heart h where h.post_id = p.id and h.user_id = logged_in_user_id) as hearted
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id
			where u.id = user_id_of_post 
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url
			order by p.heart desc , p.created desc , p.comment_count desc
			limit 20
			offset offset_value;
END;
$BODY$;





CREATE OR REPLACE FUNCTION public.get_post_page(IN offset_value bigint,IN logged_in_user_id bigint)
    RETURNS TABLE(post_id bigint, title character varying, body text, created timestamp with time zone, edited timestamp with time zone, comment_count integer, image_url character varying, heart integer, model character varying, rated boolean, user_id bigint, name character varying, profile_photo_url character varying, tags text, hearted boolean)
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100    ROWS 1000 
    
AS $BODY$

BEGIN
  
    

    RETURN QUERY
    select 
			p.id as post_id,
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags , 
			exists (select 1 from heart h where h.post_id = p.id and h.user_id = logged_in_user_id) as hearted
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id 
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url
			order by p.heart desc , p.created desc 
			limit 20
			offset offset_value;
END;
$BODY$;




drop function search_post;

CREATE OR REPLACE FUNCTION public.search_post(IN searchtitle character varying)
    RETURNS TABLE(post_id bigint, title character varying, body text, created timestamp with time zone, edited timestamp with time zone, comment_count integer, image_url character varying, heart integer, model character varying, rated boolean, user_id bigint, name character varying, profile_photo_url character varying, tags character varying, hearted boolean)
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100    ROWS 1000 
    
AS $BODY$
DECLARE
   
    title_query character varying(128);

BEGIN
  
    title_query :=  array_to_string(string_to_array(trim(SearchTitle), ' '), ' | ');

    RETURN QUERY
    SELECT 
        p.id AS post_id,
        p.title, 
        p.body,
        p.created,
        p.edited, 
        p.comment_count, 
        p.image_url,
        p.heart,
		p.model,
		p.rated,
        u.id AS user_id, 
        u.name,
        u.profile_photo_url,
		u.name as tags,
		FALSE as hearted
    FROM post p
    JOIN user_detail u ON p.user_id = u.id
    WHERE 
        to_tsvector(p.title) @@ to_tsquery(title_query)
		or 
		to_tsvector(p.body) @@ to_tsquery(title_query)
    ORDER BY 

		 CASE 
	        WHEN POSITION(LOWER(SearchTitle) IN LOWER(p.title)) = 0 THEN 9999
	        ELSE POSITION(LOWER(SearchTitle) IN LOWER(p.title))
	    END ASC,

        
        GREATEST(
            ts_rank_cd(to_tsvector('english', p.title), to_tsquery('english', title_query)),
            ts_rank_cd(to_tsvector('english', p.body), to_tsquery('english', title_query))
        ) DESC,

        
        p.created DESC

	
	
	
	
    LIMIT 10;
END;
$BODY$;