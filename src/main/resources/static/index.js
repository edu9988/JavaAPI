const clearTab = () => {
    document.querySelectorAll("tbody>tr")
	.forEach( tr => tr.remove() )
}

const clearForm = () => {
    document.querySelectorAll("form>*")
	.forEach( el => el.remove() )
}

const baseUrl = "https://javaapi-d1ez.onrender.com/api"
let endpoint = "/users"

const toHash = () => {
    document.body.style = "background-color: #ff9898"
    endpoint = "/hash"
    const bt = document.querySelector("nav>button:last-child")
    bt.onclick = toNormal
    bt.innerHTML = "Switch to normal version"
    console.log( `url now: ${baseUrl}${endpoint}` )
}

const toNormal = () => {
    document.body.style = "background-color: white"
    endpoint = "/users"
    const bt = document.querySelector("nav>button:last-child")
    bt.onclick = toHash
    bt.innerHTML = "Switch to hash version"
    console.log( `url now: ${baseUrl}${endpoint}` )
}

window.onload = () => {
    let but = document.querySelector("button")

    const tbody = document.querySelector("tbody")
    const form = document.querySelector("form")
    const par = document.querySelector("p");

    but.onclick = () => { /*Get All*/
	par.innerHTML = ""
	clearTab()
	clearForm()
	fetch( `${baseUrl}${endpoint}` )
	    .then(r => {
		if( r.status === 200 ){
		    par.innerHTML = "Ok"
		    par.classList.remove("bad")
		    par.classList.add("good")
		    return r.json()
		}
		else{
		    throw new Error( "Unkown error" )
		}
	    })
	    .then( j => {
		par.innerHTML = "Ok"
		par.classList.remove("bad")
		par.classList.add("good")
		j.forEach( u => {
		    const tr = document.createElement("tr")
		    let td = document.createElement("td")
		    td.innerHTML = u.uid
		    tr.append( td )
		    td = document.createElement("td")
		    td.innerHTML = u.uname
		    tr.append( td )
		    td = document.createElement("td")
		    td.innerHTML = u.pwd
		    tr.append( td )
		    tbody.append( tr )
		})
	    })
	    .catch( e => {
		par.innerHTML = e.message
		par.classList.remove("good")
		par.classList.add("bad")
	    })
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Get(i)*/
	par.innerHTML = ""
	clearForm()
	const label = document.createElement("label")
	label.setAttribute( "for" , "uid" )
	label.innerHTML = "Id:"
	form.append( label )
	const i = document.createElement("input")
	i.type = "number"
	i.placeholder = "Integer"
	i.id = "uid"
	i.size = "8";
	i.onchange = () => par.innerHTML = ""
	i.onkeyup = () => par.innerHTML = ""
	form.append( i )
	const b = document.createElement("button")
	b.innerHTML = "Get"
	b.onclick = e => {
	    e.preventDefault()
	    if( i.value.trim().length ){
		fetch( `${baseUrl}${endpoint}/${i.value}` )
		    .then(r => {
			if( r.status === 200 ){
			    par.innerHTML = "Ok"
			    par.classList.remove("bad")
			    par.classList.add("good")
			    return r.json()
			}
			else if( r.status === 404 ){
			    throw new Error( "Not Found" )
			}
			else{
			    throw new Error( "Unkown error" )
			}
		    })
		    .then(j => {
			clearTab()
			const tr = document.createElement("tr")
			let td = document.createElement("td")
			td.innerHTML = j.uid
			tr.append( td )
			td = document.createElement("td")
			td.innerHTML = j.uname
			tr.append( td )
			td = document.createElement("td")
			td.innerHTML = j.pwd
			tr.append( td )
			tbody.append( tr )
		    })
		    .catch(e => {
			par.innerHTML = e.message
			par.classList.remove("good")
			par.classList.add("bad")
		    })
	    } /*end if*/
	    else{
		par.innerHTML = "Empty form field"
		par.classList.remove("good")
		par.classList.add("bad")
	    }
	} /* end submit onclick */
	form.append( b )
    }/*end Get(i) onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Post(user)*/
	par.innerHTML = ""
	clearForm()
	let label = document.createElement("label")
	label.setAttribute( "for" , "uname" )
	label.innerHTML = "User:"
	form.append( label )
	const u = document.createElement("input")
	u.type = "text"
	u.id = "uname"
	u.size = "16";
	u.onchange = () => par.innerHTML = ""
	u.onkeyup = () => par.innerHTML = ""
	form.append( u )

	label = document.createElement("label")
	label.setAttribute( "for" , "pwd" )
	label.innerHTML = " Password:"
	form.append( label )
	const p = document.createElement("input")
	p.type = "text"
	p.id = "pwd"
	p.size = "16";
	p.onchange = () => par.innerHTML = ""
	p.onkeyup = () => par.innerHTML = ""
	form.append( p )

	const b = document.createElement("button")
	b.innerHTML = "Post"
	b.onclick = e => {
	    e.preventDefault()
	    if( u.value.trim().length && p.value.trim().length ){
		fetch( `${baseUrl}${endpoint}` , {
		    method:"POST",
		    mode: "cors",
		    headers:{
			"Content-Type": "application/json"
		    },
		    body: JSON.stringify({
			uname:u.value,
			pwd:p.value
		    })
		})
		    .then(r => {
			if( r.status === 201 ){
			    par.innerHTML = "Created"
			    par.classList.remove("bad")
			    par.classList.add("good")
			    return r.json()
			}
			else if( r.status === 400){
			    throw new Error( "Bad Request: invalid formatting, no spaces allowed" )
			}
			else if( r.status === 409){
			    throw new Error( "Conflict: User exists" )
			}
			else{
			    throw new Error( "Unkown error" )
			}
		    })
		    .then(j => {
			clearTab()
			clearForm()
			const tr = document.createElement("tr")
			let td = document.createElement("td")
			td.innerHTML = j.uid
			tr.append( td )
			td = document.createElement("td")
			td.innerHTML = j.uname
			tr.append( td )
			td = document.createElement("td")
			td.innerHTML = j.pwd
			tr.append( td )
			tbody.append( tr )
		    })
		    .catch(e => {
			par.innerHTML = e.message
			par.classList.remove("good")
			par.classList.add("bad")
		    })
	    } /* end if */
	    else{
		par.innerHTML = "Empty form field"
		par.classList.remove("good")
		par.classList.add("bad")
	    }
	} /* end submit onclick */
	form.append( b )
    }/*end Post onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Put(i,user)*/
	par.innerHTML = ""
	clearForm()
	let label = document.createElement("label")
	label.setAttribute( "for" , "uid" )
	label.innerHTML = "Id:"
	form.append( label )
	const i = document.createElement("input")
	i.type = "number"
	i.placeholder = "Integer"
	i.id = "uid"
	i.size = "28";
	i.onchange = () => par.innerHTML = ""
	i.onkeyup = () => par.innerHTML = ""
	form.append( i )
	label = document.createElement("label")
	label.setAttribute( "for" , "uname" )
	label.innerHTML = " User:"
	form.append( label )
	const u = document.createElement("input")
	u.type = "text"
	u.id = "uname"
	u.size = "16";
	u.onchange = () => par.innerHTML = ""
	u.onkeyup = () => par.innerHTML = ""
	form.append( u )

	label = document.createElement("label")
	label.setAttribute( "for" , "pwd" )
	label.innerHTML = " Password:"
	form.append( label )
	const p = document.createElement("input")
	p.type = "text"
	p.id = "pwd"
	p.size = "16";
	p.onchange = () => par.innerHTML = ""
	p.onkeyup = () => par.innerHTML = ""
	form.append( p )

	const b = document.createElement("button")
	b.innerHTML = "Put"
	b.onclick = e => {
	    e.preventDefault()
	    if( i.value.trim().length && u.value.trim().length && p.value.trim().length ){
		fetch( `${baseUrl}${endpoint}/${i.value}` , {
		    method:"PUT",
		    headers:{
			"Content-Type": "application/json"
		    },
		    body: JSON.stringify({
			uname:u.value,
			pwd:p.value
		    })
		})
		    .then(r => {
			if( r.status === 200 ){
			    par.innerHTML = "Ok"
			    par.classList.remove("bad")
			    par.classList.add("good")
			    return r.json()
			}
			else if( r.status === 404){
			    throw new Error( "Not Found" )
			}
			else if( r.status === 400){
			    throw new Error( "Bad Request: invalid formatting, no spaces allowed" )
			}
			else if( r.status === 409){
			    throw new Error( "Conflict: User exists" )
			}
			else{
			    throw new Error( "Unkown error" )
			}
		    })
		    .then(j => {
			clearTab()
			clearForm()
			const tr = document.createElement("tr")
			let td = document.createElement("td")
			td.innerHTML = j.uid
			tr.append( td )
			td = document.createElement("td")
			td.innerHTML = j.uname
			tr.append( td )
			td = document.createElement("td")
			td.innerHTML = j.pwd
			tr.append( td )
			tbody.append( tr )
		    })
		    .catch(e => {
			par.innerHTML = e.message
			par.classList.remove("good")
			par.classList.add("bad")
		    })
	    }
	    else{
		par.innerHTML = "Empty form field"
		par.classList.remove("good")
		par.classList.add("bad")
	    }
	}
	form.append( b )
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Delete(i)*/
	par.innerHTML = ""
	clearForm()
	const label = document.createElement("label")
	label.setAttribute( "for" , "uid" )
	label.innerHTML = "Id:"
	form.append( label )
	const i = document.createElement("input")
	i.type = "number"
	i.placeholder = "Integer"
	i.id = "uid"
	i.size = "28";
	i.onchange = () => par.innerHTML = ""
	i.onkeyup = () => par.innerHTML = ""
	form.append( i )
	const b = document.createElement("button")
	b.innerHTML = "Delete"
	b.onclick = e => {
	    e.preventDefault()
	    if( i.value.trim().length ){
		fetch( `${baseUrl}${endpoint}/${i.value}` , {
		    method:"DELETE",
		    mode: "cors"
		})
		    .then(r => {
			if( r.status === 204 ){
			    clearTab()
			    par.innerHTML = "Deleted"
			    par.classList.remove("bad")
			    par.classList.add("good")
			}
			else if( r.status === 404 ){
			    throw new Error( "Not Found" )
			}
			else{
			    throw new Error( "Unkown error" )
			}
		    })
		    .catch(e => {
			par.innerHTML = e.message
			par.classList.remove("good")
			par.classList.add("bad")
		    })
	    }
	    else{
		par.innerHTML = "Empty form field"
		par.classList.remove("good")
		par.classList.add("bad")
	    }
	}
	form.append( b )
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Authenticate(user)*/
	par.innerHTML = ""
	clearForm()
	let label = document.createElement("label")
	label.setAttribute( "for" , "uname" )
	label.innerHTML = "User:"
	form.append( label )
	const u = document.createElement("input")
	u.type = "text"
	u.id = "uname"
	u.size = "16";
	u.onchange = () => par.innerHTML = ""
	u.onkeyup = () => par.innerHTML = ""
	form.append( u )

	label = document.createElement("label")
	label.setAttribute( "for" , "pwd" )
	label.innerHTML = " Password:"
	form.append( label )
	const p = document.createElement("input")
	p.type = "text"
	p.id = "pwd"
	p.size = "16";
	p.onchange = () => par.innerHTML = ""
	p.onkeyup = () => par.innerHTML = ""
	form.append( p )

	const b = document.createElement("button")
	b.innerHTML = "Authenticate"
	b.onclick = e => {
	    e.preventDefault()
	    if( u.value.trim().length && p.value.trim().length ){
		fetch( `${baseUrl}${endpoint}/auth` , {
		    method:"POST",
		    mode: "cors",
		    headers:{
			"Content-Type": "application/json"
		    },
		    body: JSON.stringify({
			uname:u.value,
			pwd:p.value
		    })
		})
		    .then(r => {
			if( r.status === 200 ){
			    par.innerHTML = "Ok: user and password match"
			    par.classList.remove("bad")
			    par.classList.add("good")
			}
			else if( r.status === 400){
			    throw new Error( "Bad Request: invalid formatting, no spaces allowed" )
			}
			else if( r.status === 404){
			    throw new Error( "Not Found: invalid user and password combination" )
			}
			else{
			    throw new Error( "Unkown error" )
			}
		    })
		    .catch(e => {
			par.innerHTML = e.message
			par.classList.remove("good")
			par.classList.add("bad")
		    })
	    } /* end if */
	    else{
		par.innerHTML = "Empty form field"
		par.classList.remove("good")
		par.classList.add("bad")
	    }
	} /* end submit onclick */
	form.append( b )
    }/*end Post onclick*/

    but = but.nextElementSibling

    but.onclick = toHash/*Switch to hash*/
}
