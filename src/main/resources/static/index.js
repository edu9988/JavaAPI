const clearTab = () => {
    document.querySelectorAll("tbody>tr")
	.forEach( tr => tr.remove() )
}

const clearForm = () => {
    document.querySelectorAll("form>*")
	.forEach( el => el.remove() )
}


window.onload = () => {
    const url = "http://localhost:8080/api/users"

    let but = document.querySelector("button")

    const tbody = document.querySelector("tbody")
    const form = document.querySelector("form")

    but.onclick = () => { /*Get All*/
	fetch( `${url}` )
	    .then( r => r.json() )
	    .then( j => {
		clearTab()
		clearForm()
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
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Get(i)*/
	clearForm()
	const label = document.createElement("label")
	label.setAttribute( "for" , "uid" )
	label.innerHTML = "Id:"
	form.append( label )
	const i = document.createElement("input")
	i.type = "text"
	i.id = "uid"
	i.size = "28";
	form.append( i )
	const b = document.createElement("button")
	b.innerHTML = "Submit"
	b.onclick = e => {
	    e.preventDefault()
	    if( i.value.trim().length ){
		fetch( `${url}/${i.value}` )
		    .then(r => r.json() )
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
			console.log( e)
			i.style = "border: 2px solid red"
		    })
	    }
	    else
		console.log( "Empy form field" )
	}
	form.append( b )
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => {/*Post(user)*/
	clearForm()
	let label = document.createElement("label")
	label.setAttribute( "for" , "uname" )
	label.innerHTML = "User:"
	form.append( label )
	const u = document.createElement("input")
	u.type = "text"
	u.id = "uname"
	u.size = "16";
	form.append( u )

	label = document.createElement("label")
	label.setAttribute( "for" , "pwd" )
	label.innerHTML = " Password:"
	form.append( label )
	const p = document.createElement("input")
	p.type = "text"
	p.id = "pwd"
	p.size = "16";
	form.append( p )

	const b = document.createElement("button")
	b.innerHTML = "Submit"
	b.onclick = e => {
	    e.preventDefault()
	    if( u.value.trim().length && p.value.trim().length ){
		fetch( `${url}` , {
		    method:"POST",
		    mode: "cors",
		    credentials: "include",
		    headers:{
			"Content-Type": "application/json"
		    },
		    body: JSON.stringify({
			uname:u.value,
			pwd:p.value
		    })
		})
		    .then(r => r.json() )
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
		    .catch( e => {
			console.log(e)
		    })
	    }
	    else
		console.log( "Emtpy form field" )
	}
	form.append( b )
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Put(i,user)*/
	clearForm()
	let label = document.createElement("label")
	label.setAttribute( "for" , "uid" )
	label.innerHTML = "Id:"
	form.append( label )
	const i = document.createElement("input")
	i.type = "text"
	i.id = "uid"
	i.size = "28";
	form.append( i )
	label = document.createElement("label")
	label.setAttribute( "for" , "uname" )
	label.innerHTML = " User:"
	form.append( label )
	const u = document.createElement("input")
	u.type = "text"
	u.id = "uname"
	u.size = "16";
	form.append( u )

	label = document.createElement("label")
	label.setAttribute( "for" , "pwd" )
	label.innerHTML = " Password:"
	form.append( label )
	const p = document.createElement("input")
	p.type = "text"
	p.id = "pwd"
	p.size = "16";
	form.append( p )

	const b = document.createElement("button")
	b.innerHTML = "Submit"
	b.onclick = e => {
	    e.preventDefault()
	    if( i.value.trim().length && u.value.trim().length && p.value.trim().length ){
		fetch( `${url}/${i.value}` , {
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
			clearForm()
		    })
		    .catch( e => {
			console.log(e)
			i.style = "border: 2px solid red"
		    })
	    }
	    else
		console.log( "Emtpy form field" )
	}
	form.append( b )
    }/*end onclick*/

    but = but.nextElementSibling

    but.onclick = () => { /*Delete(i)*/
	clearForm()
	const label = document.createElement("label")
	label.setAttribute( "for" , "uid" )
	label.innerHTML = "Id:"
	form.append( label )
	const i = document.createElement("input")
	i.type = "text"
	i.id = "uid"
	i.size = "28";
	form.append( i )
	const b = document.createElement("button")
	b.innerHTML = "Submit"
	b.onclick = e => {
	    e.preventDefault()
	    if( i.value.trim().length ){
		fetch( `${url}/${i.value}` , { method:"DELETE" , mode: "cors"  })
		    .then(r => {
			clearForm()
		    })
		    .catch(e => {
			console.log( e)
			i.style = "border: 2px solid red"
		    })
	    }
	    else
		console.log( "Empy form field" )
	}
	form.append( b )
    }/*end onclick*/
}
